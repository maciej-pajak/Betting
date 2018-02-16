package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.Game;
import pl.maciejpajak.domain.util.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {
    
    public Optional<Game> findOneByIdAndVisible(Long id, boolean isVisible);
    
    public Collection<Game> findAllByVisible(boolean isVisible);
    public Collection<Game> findAllByCompetitionIdAndVisible(Long competitionId, boolean isVisible);
    public Collection<Game> findAllByStatusInAndVisible(Collection<GameStatus> statuses, boolean isVisible);
    
    public Collection<Game> findAllByStatusAndVisible(GameStatus status, boolean isVisible);
    public Collection<Game> findAllByStatusAndCompetitionIdAndVisible(GameStatus status, Long competitionId, boolean isVisible);
    
//    public Collection<Game> findAllByCompetitionId(Long competitionId);

}
