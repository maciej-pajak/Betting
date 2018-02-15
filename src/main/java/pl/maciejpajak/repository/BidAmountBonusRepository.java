package pl.maciejpajak.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.offers.BidAmountBonus;

public interface BidAmountBonusRepository extends JpaRepository<BidAmountBonus, Long> {
    
    public Collection<BidAmountBonus> findAllByVisible(boolean isVisible);
    public Optional<BidAmountBonus> findTopByMinimalBidIsLessThanEqualAndVisibleOrderByMinimalBidDesc(BigDecimal minimalBid, boolean isVisible);

}
