package pl.maciejpajak.testing.event.handler;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Collection;

import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.GamePart;
import pl.maciejpajak.domain.game.score.GameScore;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.game.util.BetLastCall;
import pl.maciejpajak.domain.game.util.CouponStatus;
import pl.maciejpajak.domain.game.util.EventType;
import pl.maciejpajak.domain.game.util.GameResult;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.domain.game.util.ScoreType;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetRepository;
import pl.maciejpajak.repository.GamePartRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.repository.GameScoreRepository;
import pl.maciejpajak.repository.GroupCouponRepository;
import pl.maciejpajak.repository.PartScoreRepository;
import pl.maciejpajak.service.CouponService;
import pl.maciejpajak.service.TransactionService;
import pl.maciejpajak.testing.event.EventDto;
import pl.maciejpajak.testing.event.event.GameEvent;

@Component
public class GameEventHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GameEventHandler.class);
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private GamePartRepository gamePartRepository;
    
    @Autowired
    private GameScoreRepository gameScoreRepository;
    
    @Autowired
    private PartScoreRepository partScoreRepository;
    
    @Autowired
    private BetRepository betRepository;
    
    @Autowired
    private BetResolver betResolver;
    
    @Autowired
    private GroupCouponRepository groupCouponRepository;
    
    @Autowired
    private CouponService couponService;

    @EventListener
    @Async
    public void handleGameEvent(GameEvent gameEvent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
        EventDto eventDto = gameEvent.getEventDto();
        Game game = gameRepository.findOneByIdAndVisible(eventDto.getGameId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(eventDto.getGameId()));
        
        switch (eventDto.getEventType()) {
        case GAME_START:
            startGame(game, eventDto);
            break;
        case GAME_END:
//            updateBets(game, BetLastCall.GAME_END);
            endGame(game, eventDto);
            break;
        case GAME_PART_END:
            endGamePart(game, eventDto);
            break;
        case GAME_PART_START:
            createGamePart(game, eventDto);
            break;
        case PARTY_ONE_SCORED:
        case PARTY_TWO_SCORED:
            partyOneOrTwoScored(game, eventDto);
            break;
        }
    }
    
    private Collection<Bet> updateBets(Game game, BetLastCall lastCall) {
        Collection<Bet> bets = betRepository.findAllByGameIdAndLastCallAndVisible(game.getId(), lastCall, true);
        bets.stream().forEach(b -> b.setBetable(false));
        return betRepository.save(bets);
    }
    
    private void startGame(Game game, EventDto eventDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
        Collection<Bet> bets = updateBets(game, BetLastCall.GAME_START);
        // cancel group bets which were not fully accepted before game start
        couponService.cancelUnacceptedGroupCoupons(game.getId());
        // update status
        game.setStatus(GameStatus.LIVE);
        gameRepository.save(game);
        // create initial game score 0 : 0
        gameScoreRepository.save(new GameScore(null, 0, 0, eventDto.getTime(), game));
        // create new game part
        createGamePart(game, eventDto);
        
        betResolver.resolve(game, bets);
    }

    private void endGame(Game game, EventDto eventDto) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ScriptException {
        Collection<Bet> bets = updateBets(game, BetLastCall.GAME_END);
        GameScore gs = gameScoreRepository.findTopByGameIdOrderByTimeDesc(game.getId()).orElseThrow(() -> new RuntimeException()); // TODO custom exception
        game.setEndTime(eventDto.getTime());
        game.setStatus(GameStatus.ENDED);
        game.setGameFinalScore(gs);
        
        GameResult result;
        int resDiff = gs.getPartyOneScore() - gs.getPartyTwoScore();
        if (resDiff > 0) {
            result = GameResult.PARTY_ONE_WON;
        } else if(resDiff == 0) {
            result = GameResult.DRAW;
        } else {
            result = GameResult.PARTY_TWO_WON;
        }
        game.setResult(result);
        gameRepository.save(game);
        // TODO resolve bets
        betResolver.resolve(game, bets);
    }
    
    public void partyOneOrTwoScored(Game game, EventDto eventDto) {
        GamePart gamePart = gamePartRepository.findTopByGameIdAndVisibleOrderByStartTimeDesc(game.getId(), true)
                                .orElseThrow(() -> new BaseEntityNotFoundException("could not find game part for game id = " + game.getId()));
        
        PartScore ps = partScoreRepository
                .findTopByGamePartIdOrderByTimeDesc(gamePart.getId()).orElse(
                        new PartScore(null, 0, 0, eventDto.getTime(), gamePart));
        
        // update part score
        int partyOneScored = 0;
        int partyTwoScored = 0;
        if (eventDto.getEventType().equals(EventType.PARTY_ONE_SCORED)) {
           partyOneScored = eventDto.getValue();
        } else {
            partyTwoScored = eventDto.getValue();
        }
        
        partScoreRepository.save(
                new PartScore(null, ps.getPartyOneScore() + partyOneScored, ps.getPartyTwoScore() + partyTwoScored, eventDto.getTime(), gamePart));
        
        // update game score if necessary
        if (game.getScoreType().equals(ScoreType.TOTAL_POINTS)) {
            GameScore gs = gameScoreRepository.findTopByGameIdOrderByTimeDesc(game.getId())
                    .orElseThrow(() -> new RuntimeException()); // TODO cusotm exception - this should not happen
            gameScoreRepository.save(
                    new GameScore(null, gs.getPartyOneScore() + partyOneScored, gs.getPartyTwoScore() + partyTwoScored, eventDto.getTime(), game));
        }
    }
    
    private void endGamePart(Game game, EventDto eventDto) {
        GamePart gamePart = gamePartRepository.findTopByGameIdAndVisibleOrderByStartTimeDesc(game.getId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException("could not find game part for game id = " + game.getId()));
        
        PartScore finalScore = partScoreRepository.findTopByGamePartIdOrderByTimeDesc(gamePart.getId())
                .orElseThrow(() -> new BaseEntityNotFoundException("could not find part score for game part id = " + gamePart.getId()));
        // set final score
        gamePart.setFinalPartScore(finalScore);
        // set part status to ENDED
        gamePart.setStatus(GameStatus.ENDED);
        // assess winner and set result
        GameResult partResult;
        int resDiff = finalScore.getPartyOneScore() - finalScore.getPartyTwoScore();
        int partyOneScored = 0;
        int partyTwoScored = 0;
        if (resDiff > 0) {
            partResult = GameResult.PARTY_ONE_WON;
            partyOneScored = 1;
        } else if (resDiff == 0) {
            partResult = GameResult.DRAW;
        } else {
            partResult = GameResult.PARTY_TWO_WON;
            partyTwoScored = 1;
        }
        gamePart.setResult(partResult);
        gamePartRepository.save(gamePart);
        
        // update game score if necessary
        if (game.getScoreType().equals(ScoreType.WON_PART_POINTS)) {
            GameScore gs = gameScoreRepository.findTopByGameIdOrderByTimeDesc(game.getId())
                    .orElseThrow(() -> new RuntimeException()); // TODO cusotm exception - this should not happen
            gameScoreRepository.save(
                    new GameScore(null, gs.getPartyOneScore() + partyOneScored, gs.getPartyTwoScore() + partyTwoScored, eventDto.getTime(), game));
        }
        
    }
    
    private void createGamePart(Game game, EventDto eventDto) {
        GamePart gamePart = GamePart.builder()
                .game(game)
                .duration(Duration.ofMinutes(eventDto.getValue()))
                .startTime(eventDto.getTime())
                .visible(true)
                .status(GameStatus.LIVE)
                .build();
        gamePart = gamePartRepository.save(gamePart);
        // create initial part score 0 : 0
        PartScore ps = new PartScore(null, 0, 0, eventDto.getTime(), gamePart);
        partScoreRepository.save(ps);
    }
    
    

}
