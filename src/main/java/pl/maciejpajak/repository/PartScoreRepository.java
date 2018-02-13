package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.score.PartScore;

public interface PartScoreRepository extends JpaRepository<PartScore, Long> {

    public Optional<PartScore> findTopByGamePartIdOrderByTimeDesc(Long partId);
    
}
