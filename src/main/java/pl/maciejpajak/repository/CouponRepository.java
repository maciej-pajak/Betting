package pl.maciejpajak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.UserCoupon;

public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

    public List<UserCoupon> findAllByOwnerIdAndVisible(Long id, boolean isVisible);
    
}
