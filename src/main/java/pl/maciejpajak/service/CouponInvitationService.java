package pl.maciejpajak.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.coupon.Coupon;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.domain.util.CouponStatus;
import pl.maciejpajak.dto.AcceptCouponInvitationDto;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.CouponInvitationRepository;
import pl.maciejpajak.repository.CouponRepository;
import pl.maciejpajak.repository.UserRepository;

@Service
public class CouponInvitationService {
    
    private static final Logger log = LoggerFactory.getLogger(CouponInvitationService.class);

    private final CouponInvitationRepository couponInvitationRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponInvitationService(CouponInvitationRepository couponInvitationRepository,
            UserRepository userRepository,
            TransactionService transactionService,
            CouponRepository couponRepository) {
        this.couponInvitationRepository = couponInvitationRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.couponRepository = couponRepository;
    }
    
    public CouponInvitation findbyId(Long invitationId) {
        return couponInvitationRepository.findOneByIdAndVisible(invitationId, true)
                .orElseThrow(() -> new BaseEntityNotFoundException(invitationId));
    }
    
    public Collection<CouponInvitation> findAllPending(Long userId) {
        return couponInvitationRepository.findAllByInvitedUserIdAndVisible(userId, true);
    }
    
    @Transactional
    public void acceptCouponInvitation(AcceptCouponInvitationDto invitationDto, Long userId) {       
        User user = userRepository.findOneByIdAndVisible(userId, true).orElseThrow(() -> new BaseEntityNotFoundException(userId));
        CouponInvitation invitation = couponInvitationRepository.findOneByIdAndVisible(invitationDto.getCouponInvitationId(), true)
                .orElseThrow(() -> new BaseEntityNotFoundException(userId));
        if (!invitation.getInvitedUser().getId().equals(userId)) {
            throw new AccessDeniedException("access denied");
        }
        if (invitation.getBetTransaction() != null) {
            throw new RuntimeException("invitation has already been accepted");
        }
        int unacceptedInvitations = invitation.getGroupCoupon().getUnacceptedInvitationsCount() - 1;
        invitation.getGroupCoupon().setUnacceptedInvitationsCount(unacceptedInvitations);
        log.debug("unaccepted invitations count: {} (after decrementation)", unacceptedInvitations);
        Coupon coupon = invitation.getGroupCoupon();
        if (unacceptedInvitations == 0) {
            coupon.setStatus(CouponStatus.PLACED);
        }
        log.debug("coupon value before invitation acceptance: {}", coupon.getValue());
        coupon.setValue(coupon.getValue().add(invitationDto.getAmount()));
        log.debug("coupon value after invitation acceptance: {}", coupon.getValue());
        couponRepository.save(coupon);
        invitation.setBetTransaction(transactionService.createTransaction(invitationDto.getAmount(), user, TransactionType.PLACE_BET));
        couponInvitationRepository.save(invitation);
    }
    
}
