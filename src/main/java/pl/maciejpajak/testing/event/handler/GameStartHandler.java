package pl.maciejpajak.testing.event.handler;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.GamePart;
import pl.maciejpajak.domain.game.score.GameScore;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.game.util.EventType;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.domain.game.util.ScoreType;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.GamePartRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.repository.GameScoreRepository;
import pl.maciejpajak.repository.PartScoreRepository;
import pl.maciejpajak.testing.event.EventDto;
import pl.maciejpajak.testing.event.event.GameEvent;
import pl.maciejpajak.testing.event.event.GameStartEvent;

@Component
public class GameStartHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GameStartHandler.class);
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private GamePartRepository gamePartRepository;
    
    @Autowired
    private GameScoreRepository gameScoreRepository;
    
    @Autowired
    private PartScoreRepository partScoreRepository;
    
    private void test(GameStartEvent gameStartEvent) {
        EventDto eventDto = gameStartEvent.getEventDto();
    }
    
    @EventListener
    public void handleGameStartEvent(GameStartEvent gameStartEvent) {
        EventDto eventDto = gameStartEvent.getEventDto();
        // find game
        Game game = gameRepository.findOneByIdAndVisible(eventDto.getGameId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(eventDto.getGameId()));
        // update status
        game.setStatus(GameStatus.LIVE);
        gameRepository.save(game);
        // create inital game score 0 : 0
        gameScoreRepository.save(new GameScore(null, 0, 0, eventDto.getTime(), game));
        // create new game part
        createGamePart(game, eventDto);
        
    }
    
    public void handlePartyOneOrTwoScoredEvent(GameEvent gameEvent) {
        EventDto eventDto = gameEvent.getEventDto();
        
        Game game = gameRepository.findOneByIdAndVisible(eventDto.getGameId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(eventDto.getGameId()));
        
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
    
    private void handlePartStartEvent(GameEvent gameEvent) {
        EventDto eventDto = gameEvent.getEventDto();
        
        Game game = gameRepository.findOneByIdAndVisible(eventDto.getGameId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(eventDto.getGameId()));
        
        createGamePart(game, eventDto);
        
    }
    
    private void endGamePart(Game game) {
        GamePart gamePart = gamePartRepository.findTopByGameIdAndVisibleOrderByStartTimeDesc(game.getId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException("could not find game part for game id = " + game.getId()));
        gamePart.setFinalPartScore(
                partScoreRepository.findTopByGamePartIdOrderByTimeDesc(gamePart.getId())
                    .orElseThrow(() -> new BaseEntityNotFoundException("could not find part score for game part id = " + gamePart.getId())));
        gamePartRepository.save(gamePart);
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
