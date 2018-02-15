package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.bet.BetOption;

public interface BetOptionRepository extends JpaRepository<BetOption, Long>{

    public Optional<BetOption> findOneByIdAndVisible(Long id, boolean isVisible);
    public List<BetOption> findAllByBetGameIdAndVisible(Long id, boolean isVisible);
    public List<BetOption> findAllByBetInAndVisible(Collection<Bet> bets, boolean isVisible);
    
}
