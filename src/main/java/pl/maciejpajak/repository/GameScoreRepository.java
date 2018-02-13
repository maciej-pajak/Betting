package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.score.GameScore;

public interface GameScoreRepository extends JpaRepository<GameScore, Long> {

    public Optional<GameScore> findTopByGameIdOrderByTimeDesc(Long gameId);
    
}
