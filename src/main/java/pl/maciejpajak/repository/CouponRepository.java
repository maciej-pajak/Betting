package pl.maciejpajak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
