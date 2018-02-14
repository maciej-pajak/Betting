package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.game.util.BetLastCall;

public interface BetRepository extends JpaRepository<Bet, Long> {

    public Optional<Bet> findOneByIdAndVisible(Long betId, boolean isVisible);
    
    public Collection<Bet> findAllByGameIdAndBetableAndVisible(Long gameId, boolean isBetable, boolean isVisible);
    
    public Collection<Bet> findAllByGameIdAndLastCallAndVisible(Long gameId, BetLastCall lastCall, boolean isVisible) ;
    
}
