package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.GamePart;

public interface GamePartRepository extends JpaRepository<GamePart, Long>{

    public Optional<GamePart> findTopByGameIdOrderByStartTimeDesc(Long gameId);
    
}
