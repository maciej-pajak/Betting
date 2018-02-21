package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.util.BetLastCall;

public interface BetRepository extends JpaRepository<Bet, Long> {

    public Optional<Bet> findOneByIdAndVisible(Long betId, boolean isVisible);
    
    public Collection<Bet> findAllByGameIdAndBetableAndVisible(Long gameId, boolean isBetable, boolean isVisible);
    
    public Collection<Bet> findAllByGameIdAndLastCallAndVisible(Long gameId, BetLastCall lastCall, boolean isVisible) ;
    
    public Collection<Bet> findAllByGameIdAndVisible(Long gameId, boolean isVisible);
    
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Bet b SET b.betable = :betable WHERE b.game.id = :gameId AND b.visible = :isVisible")
    public Integer updateBetableFlag(  @Param("betable") boolean betable,
                                    @Param("gameId") Long gameId,
                                    @Param("isVisible") boolean isVisible);
}
