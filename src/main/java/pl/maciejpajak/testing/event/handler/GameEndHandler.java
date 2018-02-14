package pl.maciejpajak.testing.event.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.GamePart;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.testing.event.event.GameEndEvent;

@Component
public class GameEndHandler {

    private static final Logger log = LoggerFactory.getLogger(GameEndHandler.class);
    
    @Autowired
    private GameRepository gameRepository;
   
    @Autowired
    private BetOptionRepository betOptionRepository;

    @EventListener
    public void handleGameEndEvent(GameEndEvent gameEndEvent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
        
        resolveBetOptions(gameEndEvent.getEventDto().getGameId());
    }
    
    @Transactional
    private void endGame(Long gameId) {
        Game game = gameRepository.findOneByIdAndVisible(gameId, true).orElseThrow(() -> new BaseEntityNotFoundException(gameId));
        for (GamePart part : game.getGameParts()) {
            
        }
    }
    
    private boolean resolveBetOptions(Long gameId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
        Long start = System.nanoTime();
        Long start2 = System.nanoTime();
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        log.info("create engine time: " + (System.nanoTime() - start2)/1000000 + " ms");
        Game game = gameRepository.findOneByIdAndVisible(gameId, true).orElseThrow(() -> new BaseEntityNotFoundException(gameId));
        if (game.getGameFinalScore() == null) {
            // TODO throw exception
        }
        List<BetOption> betOptions = betOptionRepository.findAllByBetGameIdAndVisible(gameId, true);
        for (BetOption bo : betOptions) {
            Pattern r = Pattern.compile("\\{([^\\}]+)\\}");
            String winCondition = bo.getWinCondition();
            // Now create matcher object.
            Matcher m = r.matcher(winCondition);
            int i = 0;
            while (m.find( )) {
                i++;
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
            
            Long stop = (System.nanoTime() - start) / 1000000;
            log.info("win condition: " + winCondition);
//            log.info("win result", engine.eval(winCondition));
            log.info("execution time " + stop + " ms");
        }

        return false;
    }
    
}
