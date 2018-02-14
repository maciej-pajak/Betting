package pl.maciejpajak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.GroupCoupon;

public interface GroupCouponRepository extends JpaRepository<GroupCoupon, Long>{

    public List<GroupCoupon> findAllByOwnerIdOrIntivationsInvitedUserIdAndVisible(Long userId, Long usrId, boolean isVisible);
    public List<GroupCoupon> findAllByOwnerIdAndVisible(Long id, boolean isVisible);
    public List<GroupCoupon> findAllByIntivationsInvitedUserIdAndVisible(Long userId, boolean isVisible);
    
}
