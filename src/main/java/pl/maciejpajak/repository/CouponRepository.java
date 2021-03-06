package pl.maciejpajak.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.coupon.Coupon;
import pl.maciejpajak.domain.util.CouponStatus;

public interface CouponRepository extends JpaRepository<Coupon, Long>{

//    public List<Coupon> findAllByOwnerIdOrIntivationsInvitedUserIdAndVisible(Long userId, Long usrId, boolean isVisible);
//    public List<Coupon> findAllByOwnerIdAndVisible(Long id, boolean isVisible);
//    public List<Coupon> findAllByIntivationsInvitedUserIdAndVisible(Long userId, boolean isVisible);
//    public Collection<Coupon> findAllByStatusAndPlacedBetsBetOptionId(CouponStatus couponStatus, Long betOptionId);
    
    public Collection<Coupon> findAllByStatusAndPlacedBetsBetOptionIdAndVisible(CouponStatus couponStatus, Long betOptionId, boolean isVisible);
    public Collection<Coupon> findAllByStatusIsAndPlacedBetsBetOptionBetInAndVisible(CouponStatus couponStatus, Collection<Bet> bets, boolean isVisible);
}
