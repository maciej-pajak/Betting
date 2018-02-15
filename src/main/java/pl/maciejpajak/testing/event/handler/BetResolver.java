package pl.maciejpajak.testing.event.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.Coupon;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.util.BetOptionStatus;
import pl.maciejpajak.domain.game.util.CouponStatus;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.BetRepository;
import pl.maciejpajak.repository.CouponRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.service.TransactionService;

@Component
public class BetResolver {

    private static final Logger log = LoggerFactory.getLogger(BetResolver.class);
    
    @Autowired
    private GameRepository gameRepository;
   
    @Autowired
    private BetOptionRepository betOptionRepository;
    
    @Autowired
    private BetRepository betRepository;
    
    @Autowired
    private CouponRepository couponRepository;
    
    @Autowired
    private TransactionService transactionService;

//    public void resolve(Game game, BetLastCall lastCall) throws ScriptException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//    @Async
    public void resolve(Game game, Collection<Bet> bets) throws ScriptException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // make bets with lastCall unbetable
//        Collection<Bet> bets = betRepository.findAllByGameIdAndLastCallAndVisible(game.getId(), lastCall, true);
//        bets.forEach(b -> b.setBetable(false));
//        betRepository.save(bets);
        
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
            log.debug("parsed win condition: {}", winCondition);
            log.debug("win result: {}", isWinConditionSatisfied);
            
            bo.setStatus(isWinConditionSatisfied ? BetOptionStatus.WON : BetOptionStatus.LOST);
            
            updateCouponByBetOption(bo);
        }
        betOptionRepository.save(betOptions);
        
        Long stop = (System.nanoTime() - start) / 1000000;
        log.debug("bets resolving time {} ms", stop);
    }

    private void updateCouponByBetOption(BetOption betOption) {
        Collection<Coupon> coupons = couponRepository.findAllByPlacedBetsBetOptionId(betOption.getId()); // TODO maybe add byStatus = UNRESOLVED?
        coupons.forEach(c -> {
            c.setUnsersolvedBetsCount(c.getUnsersolvedBetsCount() - 1);
            couponRepository.save(c);
            if (c.getUnsersolvedBetsCount() == 0) {
                resolveCoupon(c);
            } 
        });
    }
    
    private void resolveCoupon(Coupon coupon) {
        if (isCouponWon(coupon)) {
            coupon.setStatus(CouponStatus.WON);
            
            
            
            payOutPrize(coupon);
        } else {
            coupon.setStatus(CouponStatus.WON);
            coupon.setBonus(BigDecimal.ZERO);
            coupon.setTotalPrize(BigDecimal.ZERO);
        }
        
        couponRepository.save(coupon); // TODO check if this works
    }
    
    private boolean isCouponWon(Coupon coupon) {
        boolean isCouponWon = true;
        for (PlacedBet pb : coupon.getPlacedBets()) {
            if(pb.getBetOption().getStatus().equals(BetOptionStatus.LOST)) {
                isCouponWon = false;
                break;
            }
        }
        return isCouponWon;
    }
    
    private void payOutPrize(Coupon coupon) {
        BigDecimal totalPrize = coupon.getValue();
        for (PlacedBet pb : coupon.getPlacedBets()) {
            totalPrize = totalPrize.multiply(pb.getOdd().getValue());
        }
//        totalPrize = totalPrize.multiply(BONUS);
        coupon.setTotalPrize(totalPrize);
        
        if (coupon instanceof GroupCoupon) {
            Map<User, BigDecimal> usersAmounts = new HashMap<>();
            usersAmounts.put(coupon.getOwner(), coupon.getOwnerTransaction().getAmount());
            ((GroupCoupon) coupon).getIntivations().forEach(inv -> {
                usersAmounts.put(inv.getInvitedUser(), inv.getBetTransaction().getAmount());
            });
//            BigDecimal usersAmounts.values().stream().reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
            usersAmounts.forEach((u, a) -> 
                transactionService.createTransaction(coupon.getTotalPrize().multiply(a).divide(coupon.getValue()), u, TransactionType.WIN));
        } else {
            transactionService.createTransaction(coupon.getTotalPrize(), coupon.getOwner(), TransactionType.WIN);
        }
    }
    
}
