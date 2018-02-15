package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.game.util.CouponStatus;

public interface GroupCouponRepository extends JpaRepository<GroupCoupon, Long>{

    public List<GroupCoupon> findAllByOwnerIdOrIntivationsInvitedUserIdAndVisible(Long userId, Long usrId, boolean isVisible);
    public List<GroupCoupon> findAllByOwnerIdAndVisible(Long id, boolean isVisible);
    public List<GroupCoupon> findAllByIntivationsInvitedUserIdAndVisible(Long userId, boolean isVisible);
//    public List<GroupCoupon> findAllByStatusPENDINGAndVisible(boolean isVisible);
//    public List<GroupCoupon> findAllByStatusAndPlacedBetsBetsInAndVisible(CouponStatus couponStatus, Collection<Bet> bets, boolean isVisible);
}
