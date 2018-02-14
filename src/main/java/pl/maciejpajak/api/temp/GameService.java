package pl.maciejpajak.api.temp;

import java.util.Collection;

import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.game.score.GameScore;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.GamePartRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.repository.GameScoreRepository;
import pl.maciejpajak.repository.PartScoreRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameScoreRepository gameScoreRepository;
    private final GamePartRepository gamePartRepository;
    private final PartScoreRepository partScoreRepository;
    
    public GameService(GameRepository gameRepository,
            GameScoreRepository gameScoreRepository,
            GamePartRepository gamePartRepository,
            PartScoreRepository partScoreRepository) {
        this.gameRepository = gameRepository;
        this.gameScoreRepository = gameScoreRepository;
        this.gamePartRepository = gamePartRepository;
        this.partScoreRepository = partScoreRepository;
    }
    
    public Game findOneById(Long id) {
        return gameRepository.findOneByIdAndVisible(id, true).orElseThrow(() -> new BaseEntityNotFoundException(id));
    }
    
    public Collection<Game> findAll() {
        return gameRepository.findAllByVisible(true);
    }
    
    public Collection<Game> findAllByCompetitionId(Long competitionId) {
        return gameRepository.findAllByCompetitionIdAndVisible(competitionId, true);
    }
    
    public Collection<Game> getAllLiveGamesByCompetition(Long competitionId) {
        if (competitionId == null) {
            return gameRepository.findAllByStatusAndVisible(GameStatus.LIVE, true);
        } else {
            return gameRepository.findAllByStatusAndCompetitionIdAndVisible(GameStatus.LIVE, competitionId, true);
        }
    }
    
    public GameScore findLatestGameScoreByGameId(Long gameId) {
        return gameScoreRepository.findTopByGameIdOrderByTimeDesc(gameId).orElseThrow(() -> new BaseEntityNotFoundException(gameId));
    }
    
    public PartScore findLatestPartScore(Long gameId, Long partId) {
        if (partId == null) {
            partId  = gamePartRepository.findTopByGameIdAndVisibleOrderByStartTimeDesc(gameId, true)
                    .orElseThrow(() -> new BaseEntityNotFoundException(gameId))
                    .getId();
        }
        Long partId2 = partId; // TODO ??????????? why this error
        // TODO in else: mayble validate to check if GamePart with partId is a part of Game with gameId 
        return partScoreRepository.findTopByGamePartIdOrderByTimeDesc(partId).orElseThrow(() -> new BaseEntityNotFoundException(partId2));
    }
    
}
