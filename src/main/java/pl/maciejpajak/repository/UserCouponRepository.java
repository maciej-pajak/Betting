package pl.maciejpajak.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    public Optional<UserCoupon> findOneByIdAndVisible(Long id, boolean isVisible);
    public List<UserCoupon> findAllByOwnerIdAndVisible(Long id, boolean isVisible);
    
}
