package pl.maciejpajak.api.temp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.BetOptionWithOddDto;
import pl.maciejpajak.api.dto.CouponPlaceDto;
import pl.maciejpajak.api.dto.CouponShowDto;
import pl.maciejpajak.api.dto.GroupCouponPlaceDto;
import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.Coupon;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.coupon.UserCoupon;
import pl.maciejpajak.domain.game.util.CouponStatus;
import pl.maciejpajak.domain.game.util.GameStatus;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.exception.BetClosedException;
import pl.maciejpajak.exception.BetsCombinationNotAllowedException;
import pl.maciejpajak.exception.GameHasAlreadyStartedException;
import pl.maciejpajak.exception.OddHasChangedException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.CouponInvitationRepository;
import pl.maciejpajak.repository.CouponRepository;
import pl.maciejpajak.repository.GroupCouponRepository;
import pl.maciejpajak.repository.OddRepository;
import pl.maciejpajak.repository.UserCouponRepository;
import pl.maciejpajak.repository.UserRepository;
import pl.maciejpajak.service.TransactionService;


@Service
public class CouponService {

    private static final Logger log = LoggerFactory.getLogger(CouponService.class);

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
        if (couponDto instanceof GroupCouponPlaceDto) {
            GroupCoupon groupCoupon = GroupCoupon.builder()
                    .created(LocalDateTime.now())
                    .placedBets(prepareAndValidateBets(couponDto))
                    .owner(user)
                    .ownerTransaction(transactionService.createTransaction(couponDto.getAmount(), user, TransactionType.PLACE_BET))
                    .status(CouponStatus.PENDING)
                    .visible(true)
                    .build();
            groupCoupon.setUnsersolvedBetsCount(groupCoupon.getPlacedBets().size());
            groupCoupon.setValue(groupCoupon.getOwnerTransaction().getAmount()); // initial value, this will be increased when coupon inviatation is accepted
            
            groupCoupon.getPlacedBets().forEach(pb -> pb.setCoupon(groupCoupon));
            // TODO add total value
        
            sendInvitations(groupCoupon, (GroupCouponPlaceDto) couponDto);
            groupCouponRepository.saveAndFlush(groupCoupon);
        } else {
            UserCoupon userCoupon = UserCoupon.builder()
                    .created(LocalDateTime.now())
                    .placedBets(prepareAndValidateBets(couponDto))
                    .owner(user)
                    .ownerTransaction(transactionService.createTransaction(couponDto.getAmount(), user, TransactionType.PLACE_BET))
                    .status(CouponStatus.PLACED)
                    .visible(true)
                    .build();
            userCoupon.setValue(userCoupon.getOwnerTransaction().getAmount());
            userCoupon.setUnsersolvedBetsCount(userCoupon.getPlacedBets().size());
            userCoupon.getPlacedBets().forEach(pb -> pb.setCoupon(userCoupon));
            
            userCouponRepository.saveAndFlush(userCoupon);
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
                throw new RuntimeException("user cannot invite himself"); // TODO define exception - user cannot invite himself
            }
            invitations.add(
                   CouponInvitation.builder()
                       .invitedUser(userRepository.findOneById(id).orElseThrow(() -> new BaseEntityNotFoundException(id)))
                       .visible(true)
                       .groupCoupon(coupon)
                       .build()
                    );
        }
        if (invitations.isEmpty()) {
            throw new RuntimeException("no user is invited for this bet");
        }
        coupon.setUnacceptedInvitationsCount(invitations.size());
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
        
        if (placedBets.isEmpty()) {
            throw new RuntimeException("there are no bets on coupon");
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
    
//    @Transactional
//    public void acceptCouponInvitation(Long userId, Long invitationId, BigDecimal amount) {
//        
//        User user = userRepository.findOneByIdAndVisible(userId, true).orElseThrow(() -> new BaseEntityNotFoundException(userId));
//        CouponInvitation invitation = couponInvitationRepository.findOneByIdAndVisible(invitationId, true)
//                .orElseThrow(() -> new BaseEntityNotFoundException(userId));
//        if (!invitation.getInvitedUser().getId().equals(userId)) {
//            throw new AccessDeniedException("access denied"); // TODO rethink
//        }
//        if (invitation.getBetTransaction() != null) {
//            throw new RuntimeException("invitation has already been accepted");
//        }
//        int unacceptedInvitations = invitation.getGroupCoupon().getUnacceptedInvitationsCount() - 1;
//        invitation.getGroupCoupon().setUnacceptedInvitationsCount(unacceptedInvitations);
//        log.debug("unaccepted invitations count: {} (after decrementation)", unacceptedInvitations);
//        Coupon coupon = invitation.getGroupCoupon();
//        if (unacceptedInvitations == 0) {   // TODO check if this persists to db
//            coupon.setStatus(CouponStatus.PLACED);
//        }
//        coupon.setValue(coupon.getValue().add(amount));
//        couponRepository.save(coupon);
//        invitation.setBetTransaction(transactionService.createTransaction(amount, user, TransactionType.PLACE_BET));
//        couponInvitationRepository.save(invitation);
//    }
    
    @Autowired // FIXME
    private CouponRepository couponRepository;
    
    //TODO maybe async?
    public void cancelUnacceptedGroupCoupons(Collection<Bet> bets) {
//        groupCouponRepository.findAllByStatusAndPlacedBetsBetsInAndVisible(CouponStatus.PENDING, bets, true)
        couponRepository.findAllByStatusIsAndPlacedBetsBetOptionBetInAndVisible(CouponStatus.PENDING, bets, true)
            .stream()
            .filter(c -> (c instanceof GroupCoupon))
            .map(c -> (GroupCoupon) c)
            .forEach(this::cancelGroupCoupon); // TODO Add bets
    }
    
    private void cancelGroupCoupon(GroupCoupon groupCoupon) {
        log.debug("cancelling coupon with id = {}", groupCoupon.getId());
        groupCoupon.getIntivations().forEach(inv -> {
            Transaction t = inv.getBetTransaction();
            if (t != null) {
                transactionService.createTransaction(t.getAmount(), t.getOwner(), TransactionType.CANCEL_BET);
            }
        });
        groupCoupon.setStatus(CouponStatus.CANCELLED);
        groupCouponRepository.save(groupCoupon);
    }
}
