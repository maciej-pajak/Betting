package pl.maciejpajak.engine;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.GamePart;
import pl.maciejpajak.domain.game.score.GameScore;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.util.BetLastCall;
import pl.maciejpajak.domain.util.EventType;
import pl.maciejpajak.domain.util.GameResult;
import pl.maciejpajak.domain.util.GameStatus;
import pl.maciejpajak.domain.util.ScoreType;
import pl.maciejpajak.dto.EventDto;
import pl.maciejpajak.event.GameEvent;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.BetRepository;
import pl.maciejpajak.repository.GamePartRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.repository.GameScoreRepository;
import pl.maciejpajak.repository.PartScoreRepository;
import pl.maciejpajak.service.CouponService;

@Component
public class GameEventHandler {

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
    private CouponService couponService;

    /**
     * Listens for {@code GameEvent} and updates games, game parts and scores.
     * 
     * @param gameEvent
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ScriptException
     */
    @EventListener
    @Async
    public void handleGameEvent(GameEvent gameEvent)
            throws IllegalAccessException, InvocationTargetException, ScriptException {
        EventDto eventDto = gameEvent.getEventDto();
        Game game = gameRepository.findOneByIdAndVisible(eventDto.getGameId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(eventDto.getGameId()));

        switch (eventDto.getEventType()) {
        case GAME_START:
            startGame(game, eventDto);
            break;
        case GAME_END:
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

    private void updateBets(Game game, BetLastCall lastCall) {
        betRepository.updateBetableFlag(false, game.getId(), lastCall, true);
    }

    private void startGame(Game game, EventDto eventDto) {
        updateBets(game, BetLastCall.GAME_START);
        // cancel group bets which were not fully accepted before game start
        couponService.cancelUnacceptedGroupCoupons(game.getId());
        // update status
        game.setStatus(GameStatus.LIVE);
        gameRepository.save(game);
        // create initial game score 0 : 0
        gameScoreRepository.save(new GameScore(null, 0, 0, eventDto.getTime(), game));
        // create new game part
        createGamePart(game, eventDto);
    }

    private void endGame(Game game, EventDto eventDto)
            throws IllegalAccessException, InvocationTargetException, ScriptException {
        updateBets(game, BetLastCall.GAME_END);
        GameScore gs = gameScoreRepository.findTopByGameIdOrderByTimeDesc(game.getId())
                .orElseThrow(() -> new RuntimeException("no game score found for game with id = " + game.getId()));
        game.setEndTime(eventDto.getTime());
        game.setStatus(GameStatus.ENDED);
        game.setGameFinalScore(gs);

        GameResult result;
        int resDiff = gs.getPartyOneScore() - gs.getPartyTwoScore();
        if (resDiff > 0) {
            result = GameResult.PARTY_ONE_WON;
        } else if (resDiff == 0) {
            result = GameResult.DRAW;
        } else {
            result = GameResult.PARTY_TWO_WON;
        }
        game.setResult(result);
        gameRepository.save(game);

        betResolver.resolve(game, betRepository.findAllByGameIdAndVisible(game.getId(), true));
    }

    public void partyOneOrTwoScored(Game game, EventDto eventDto) {
        GamePart gamePart = gamePartRepository.findTopByGameIdAndVisibleOrderByStartTimeDesc(game.getId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(
                        "could not find game part for game id = " + game.getId()));

        PartScore ps = partScoreRepository.findTopByGamePartIdOrderByTimeDesc(gamePart.getId())
                .orElse(new PartScore(null, 0, 0, eventDto.getTime(), gamePart));
        // update part score
        int partyOneScored = 0;
        int partyTwoScored = 0;
        if (eventDto.getEventType().equals(EventType.PARTY_ONE_SCORED)) {
            partyOneScored = eventDto.getValue();
        } else {
            partyTwoScored = eventDto.getValue();
        }

        partScoreRepository.save(new PartScore(null, ps.getPartyOneScore() + partyOneScored,
                ps.getPartyTwoScore() + partyTwoScored, eventDto.getTime(), gamePart));

        // update game score if necessary
        if (game.getScoreType().equals(ScoreType.TOTAL_POINTS)) {
            GameScore gs = gameScoreRepository.findTopByGameIdOrderByTimeDesc(game.getId())
                    .orElseThrow(() -> new RuntimeException("game score not found"));
            gameScoreRepository.save(new GameScore(null, gs.getPartyOneScore() + partyOneScored,
                    gs.getPartyTwoScore() + partyTwoScored, eventDto.getTime(), game));
        }
    }

    private void endGamePart(Game game, EventDto eventDto) {
        GamePart gamePart = gamePartRepository.findTopByGameIdAndVisibleOrderByStartTimeDesc(game.getId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(
                        "could not find game part for game id = " + game.getId()));

        PartScore finalScore = partScoreRepository.findTopByGamePartIdOrderByTimeDesc(gamePart.getId())
                .orElseThrow(() -> new BaseEntityNotFoundException(
                        "could not find part score for game part id = " + gamePart.getId()));
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
                    .orElseThrow(() -> new RuntimeException("game score not found"));
            gameScoreRepository.save(new GameScore(null, gs.getPartyOneScore() + partyOneScored,
                    gs.getPartyTwoScore() + partyTwoScored, eventDto.getTime(), game));
        }

    }

    private void createGamePart(Game game, EventDto eventDto) {
        GamePart gamePart = GamePart.builder().game(game).duration(Duration.ofMinutes(eventDto.getValue()))
                .startTime(eventDto.getTime()).visible(true).status(GameStatus.LIVE).build();
        gamePart = gamePartRepository.save(gamePart);
        // create initial part score 0 : 0
        PartScore ps = new PartScore(null, 0, 0, eventDto.getTime(), gamePart);
        partScoreRepository.save(ps);
    }

}
