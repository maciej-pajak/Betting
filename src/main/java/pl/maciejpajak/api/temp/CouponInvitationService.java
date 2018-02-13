package pl.maciejpajak.api.temp;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.CouponInvitationRepository;
import pl.maciejpajak.repository.TransactionRepository;
import pl.maciejpajak.repository.UserRepository;

@Service
public class CouponInvitationService {

    private final CouponInvitationRepository couponInvitationRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CouponInvitationService(CouponInvitationRepository couponInvitationRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository) {
        this.couponInvitationRepository = couponInvitationRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }
    
    public CouponInvitation findbyId(Long invitationId) {
        return couponInvitationRepository.findOneByIdAndVisible(invitationId, true)
                .orElseThrow(() -> new BaseEntityNotFoundException(invitationId));
    }
    
    public Collection<CouponInvitation> findAllPending() {
        Long userId = 1L;   // TODO change to current user
        return couponInvitationRepository.findAllByInvitedUserIdAndVisible(userId, true);
    }
    
    public void acceptCouponInvitation(Long userId, Long invitationId) {
        User user = userRepository.findOneByIdAndVisible(userId, true).orElseThrow(() -> new BaseEntityNotFoundException(userId));
        
    }
    
}
