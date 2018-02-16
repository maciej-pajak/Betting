package pl.maciejpajak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.util.CouponStatus;

public interface GroupCouponRepository extends JpaRepository<GroupCoupon, Long>{

    public List<GroupCoupon> findAllByOwnerIdOrIntivationsInvitedUserIdAndVisible(Long userId, Long usrId, boolean isVisible);
    public List<GroupCoupon> findAllByOwnerIdAndVisible(Long id, boolean isVisible);
    public List<GroupCoupon> findAllByIntivationsInvitedUserIdAndVisible(Long userId, boolean isVisible);
    
    public List<GroupCoupon> findAllByPlacedBetsBetOptionBetGameIdAndStatusAndVisible(Long gameId, CouponStatus couponStatus, boolean isVisible);
}
