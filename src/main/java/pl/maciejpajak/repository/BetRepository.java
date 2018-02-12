package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.bet.Bet;

public interface BetRepository extends JpaRepository<Bet, Long> {

    public Optional<Bet> findOneByIdAndVisible(Long betId, boolean isVisible);
    
    public Collection<Bet> findAllByGameIdAndVisible(Long gameId, boolean isVisible);
    
}
