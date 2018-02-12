package pl.maciejpajak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.GroupCoupon;

public interface GroupCouponRepository extends JpaRepository<GroupCoupon, Long>{

}
