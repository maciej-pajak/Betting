package pl.maciejpajak.testing.event.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.GamePart;
import pl.maciejpajak.domain.game.util.BetLastCall;
import pl.maciejpajak.domain.game.util.BetOptionStatus;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.BetRepository;
import pl.maciejpajak.repository.GameRepository;

@Component
public class BetResolver {

    private static final Logger log = LoggerFactory.getLogger(BetResolver.class);
    
    @Autowired
    private GameRepository gameRepository;
   
    @Autowired
    private BetOptionRepository betOptionRepository;
    
    @Autowired
    private BetRepository betRepository;

//    @EventListener
//    public void handleGameEndEvent(GameEvent gameEndEvent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
//        
//        resolveBetOptions(gameEndEvent.getEventDto().getGameId());
//    }
    
    @Async
    public void resolve(Game game, BetLastCall lastCall) throws ScriptException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // make bets with lastCall unbetable
        Collection<Bet> bets = betRepository.findAllByGameIdAndLastCallAndVisible(game.getId(), lastCall, true);
        bets.forEach(b -> b.setBetable(false));
        betRepository.save(bets);
        
        Long start = System.nanoTime();
        
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        
        List<BetOption> betOptions = betOptionRepository.findAllByBetInAndVisible(bets, true);

        for (BetOption bo : betOptions) {
            String winCondition = bo.getWinCondition();
            Pattern r = Pattern.compile("\\{([^\\}]+)\\}");
            Matcher m = r.matcher(winCondition);
            while (m.find( )) {
               Object o = game;
               String[] names = m.group(1).split("\\.");
               for (int j = 0 ; j < names.length - 1 ; j++) {
                   String name = names[j+1];
                   Method method = Arrays.asList(o.getClass().getMethods()).stream()
                           .filter(meth -> meth.getName().equalsIgnoreCase("get" + name)).findFirst().orElseThrow(() -> new RuntimeException());
                   o = method.invoke(o, null);
               }
               winCondition = winCondition.replace(m.group(0), o.toString());               
            }
            boolean isWinConditionSatisfied = (boolean) engine.eval(winCondition);
            log.debug("initial win condition: {}", bo.getWinCondition());
            log.debug("parseg win condition: {}", winCondition);
            log.debug("win result: {}", isWinConditionSatisfied);
            
            bo.setStatus(isWinConditionSatisfied ? BetOptionStatus.WON : BetOptionStatus.LOST);
        }
        betOptionRepository.save(betOptions);

        Long stop = (System.nanoTime() - start) / 1000000;
        log.debug("bet resolving time {} ms", stop);
    }
    
}
