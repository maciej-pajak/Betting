package pl.maciejpajak.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.coupon.CouponInvitation;

public interface CouponInvitationRepository extends JpaRepository<CouponInvitation, Long> {

    public Optional<CouponInvitation> findOneByIdAndVisible(Long id, boolean isVisible);
    public Collection<CouponInvitation> findAllByInvitedUserIdAndVisible(Long userId, boolean isVisible);
    
}
