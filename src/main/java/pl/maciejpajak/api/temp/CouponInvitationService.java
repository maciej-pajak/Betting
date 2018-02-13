package pl.maciejpajak.api.temp;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.AcceptCouponInvitationDto;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.CouponInvitationRepository;
import pl.maciejpajak.repository.UserRepository;
import pl.maciejpajak.service.TransactionService;

@Service
public class CouponInvitationService {

    private final CouponInvitationRepository couponInvitationRepository;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @Autowired
    public CouponInvitationService(CouponInvitationRepository couponInvitationRepository,
            UserRepository userRepository,
            TransactionService transactionService) {
        this.couponInvitationRepository = couponInvitationRepository;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
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
        Long invitationId = invitationDto.getCouponInvitationId();
        CouponInvitation invitation = couponInvitationRepository.findOneByIdAndVisible(invitationId, true)
                .orElseThrow(() -> new BaseEntityNotFoundException(invitationId));
        if (!invitation.getInvitedUser().getId().equals(userId)) {
            throw new AccessDeniedException("current user not authorized to accept invitation wit id=" + invitationId);
        }
        User user = userRepository.findOneByIdAndVisible(userId, true).orElseThrow(() -> new BaseEntityNotFoundException(userId));
        Transaction transaction = transactionService.createTransaction(invitationDto.getAmount(), user, TransactionType.PLACE_BET);
        invitation.setBetTransaction(transaction);
        couponInvitationRepository.save(invitation);
    }
    
}
