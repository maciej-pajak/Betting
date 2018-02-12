package pl.maciejpajak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.UserCoupon;

public interface CouponRepository extends JpaRepository<UserCoupon, Long> {

}
