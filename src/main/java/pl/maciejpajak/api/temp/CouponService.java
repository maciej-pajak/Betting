package pl.maciejpajak.api.temp;

import static org.hamcrest.CoreMatchers.nullValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.cfg.beanvalidation.GroupsPerOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.BetOptionWithOddDto;
import pl.maciejpajak.api.dto.CouponPlaceDto;
import pl.maciejpajak.api.dto.CouponShowDto;
import pl.maciejpajak.api.dto.GroupCouponPlaceDto;
import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.coupon.UserCoupon;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.exception.BetClosedException;
import pl.maciejpajak.exception.BetsCombinationNotAllowedException;
import pl.maciejpajak.exception.GameHasAlreadyStartedException;
import pl.maciejpajak.exception.OddHasChangedException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.CouponInvitationRepository;
import pl.maciejpajak.repository.UserCouponRepository;
import pl.maciejpajak.repository.GroupCouponRepository;
import pl.maciejpajak.repository.OddRepository;
import pl.maciejpajak.repository.UserRepository;
import pl.maciejpajak.service.TransactionService;

@Service
public class CouponService {

    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private GroupCouponRepository groupCouponRepository;
    @Autowired
    private OddRepository oddRepository;
    @Autowired
    private BetOptionRepository betOptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CouponInvitationRepository couponInvitationRepository;

    public Collection<CouponShowDto> findAllIndividualCoupons(Long userId) {
        return null; // TODO
    }
    
    public Collection<CouponShowDto> findAllGroupCoupons(Long userId) {
        return groupCouponRepository.findAllByOwnerIdOrIntivationsInvitedUserIdAndVisible(userId, userId, true)
                .stream()
                .map(DtoMappers.convertUserCouponToDto).collect(Collectors.toList());
    }
    
    public Collection<CouponShowDto> findOwnedGroupCoupons(Long userId) {
        return groupCouponRepository.findAllByOwnerIdAndVisible(userId, true).stream()
                .map(DtoMappers.convertUserCouponToDto).collect(Collectors.toList());
    }
    
    public Collection<CouponShowDto> findInvitedGroupCoupons(Long userId) {
        return groupCouponRepository.findAllByIntivationsInvitedUserIdAndVisible(userId, true).stream()
                .map(DtoMappers.convertUserCouponToDto).collect(Collectors.toList());
    }
//    public CouponShowDto findOneById(Long id) {
//        UserCoupon userCoupon = couponRepository.findOneByIdAndVisible(id, true)
//                .orElseThrow(() -> new BaseEntityNotFoundException(userId));
//        if (userCoupon.getOwner().getId().equals(obj))
//        return 
//    }
    
    @Transactional
    public void createCoupon(CouponPlaceDto couponDto, Long userId) {
        User user = userRepository.findOneById(userId).orElseThrow(() -> new BaseEntityNotFoundException(userId));

        GroupCoupon coupon = new GroupCoupon(LocalDateTime.now(),
                prepareAndValidateBets(couponDto),
                user,
                transactionService.createTransaction(couponDto.getAmount(), user, TransactionType.PLACE_BET));
        
        coupon.getPlacedBets().forEach(pb -> pb.setCoupon(coupon));
        // TODO add total value
        if (couponDto instanceof GroupCouponPlaceDto) {
            sendInvitations(coupon, (GroupCouponPlaceDto) couponDto);
            groupCouponRepository.saveAndFlush(coupon);
        } else {
//            couponRepository.saveAndFlush((UserCoupon) coupon); // FIXME
        }
    }

    private void sendInvitations(GroupCoupon coupon, GroupCouponPlaceDto groupCouponDto) {
        List<Long> invitedIds = groupCouponDto.getInvitedUsersIds();
        if (invitedIds.isEmpty()) {
            return;
        }
        Set<CouponInvitation> invitations = new HashSet<>();
        
        for (Long id : invitedIds) {
            if (id.equals(coupon.getOwner().getId())) {
                throw new RuntimeException(); // TODO define exception - user cannot invite himself
            }
            invitations.add(
                   CouponInvitation.builder()
                       .invitedUser(userRepository.findOneById(id).orElseThrow(() -> new BaseEntityNotFoundException(id)))
                       .visible(true)
                       .groupCoupon(coupon)
                       .build()
                    );
        }
        coupon.setIntivations(invitations);
    }
       
    
    
    private Set<PlacedBet> prepareAndValidateBets(CouponPlaceDto couponDto) {
        Set<PlacedBet> placedBets = new HashSet<>();
        
        // prepare single bets list with odds
        for (BetOptionWithOddDto b : couponDto.getBetOptionsWithOdds()) {
            BetOption betOption = betOptionRepository.findOneByIdAndVisible(b.getBetOptionId(), true)
                                    .orElseThrow(() -> new BaseEntityNotFoundException(b.getBetOptionId()));
            if (!betOption.getBet().isBetable()) { // TODO check this
                throw new BetClosedException(betOption.getBet().getId());
            }
            if (couponDto instanceof GroupCouponPlaceDto) { // TODO check this
                if(!betOption.getBet().getGame().getStatus().equals(GameStatus.UPCOMING)) {
                    throw new GameHasAlreadyStartedException();
                }
            }
//            Odd odd = oddRepository.findOneById(b.getOddId())
//                    .orElseThrow(() -> new BaseEntityNotFoundException(b.getBetOptionId()));
            Odd latestOdd = oddRepository.findFirstByBetOptionIdOrderByCreatedDesc(b.getBetOptionId())
                    .orElseThrow(() -> new BaseEntityNotFoundException(b.getBetOptionId()));
            // if odd has changes since coupon was send and user doesn't accept odd change throw exception
            if (!b.getOddId().equals(latestOdd.getId()) && !couponDto.isOddsChangeAccepted()) {
                throw new OddHasChangedException();
            }
            
            placedBets.add(
                    PlacedBet.builder()
                        .visible(true)
                        .betOption(betOption)
                        .odd(latestOdd)
                        .build()
                    );
        }
        
        // validate if bets can be combined (only one bet option from each bet)
        Set<Long> betsIds = new HashSet<>();
        placedBets.forEach(pb -> {
            Long id = pb.getBetOption().getBet().getId();
            if (betsIds.contains(id)) {
                throw new BetsCombinationNotAllowedException(); // TODO add message
            } else {
                betsIds.add(id);
            }
        });
        
        return placedBets;
    }
    
    @Transactional
    public void acceptCouponInvitation(Long userId, Long invitationId, BigDecimal amount) {
        User user = userRepository.findOneByIdAndVisible(userId, true).orElseThrow(() -> new BaseEntityNotFoundException(userId));
        CouponInvitation invitation = couponInvitationRepository.findOneByIdAndVisible(invitationId, true)
                .orElseThrow(() -> new BaseEntityNotFoundException(userId));
        if (!invitation.getInvitedUser().getId().equals(userId)) {
            throw new AccessDeniedException("access denied"); // TODO rethink
        }
        if (invitation.getBetTransaction() != null) {
            throw new RuntimeException("invitation has already been accepted");
        }
        invitation.setBetTransaction(transactionService.createTransaction(amount, user, TransactionType.PLACE_BET));
        couponInvitationRepository.save(invitation);
    }
}
