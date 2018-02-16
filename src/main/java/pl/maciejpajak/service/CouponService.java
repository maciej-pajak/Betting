package pl.maciejpajak.service;

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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.coupon.UserCoupon;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.domain.util.CouponStatus;
import pl.maciejpajak.domain.util.GameStatus;
import pl.maciejpajak.domain.util.NotificationType;
import pl.maciejpajak.dto.BetOptionWithOddDto;
import pl.maciejpajak.dto.BidAmountBonusDto;
import pl.maciejpajak.dto.CouponPlaceDto;
import pl.maciejpajak.dto.CouponShowDto;
import pl.maciejpajak.dto.GroupCouponPlaceDto;
import pl.maciejpajak.event.NotifyUserEvent;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.exception.BetClosedException;
import pl.maciejpajak.exception.BetsCombinationNotAllowedException;
import pl.maciejpajak.exception.GameHasAlreadyStartedException;
import pl.maciejpajak.exception.OddHasChangedException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.BidAmountBonusRepository;
import pl.maciejpajak.repository.GroupCouponRepository;
import pl.maciejpajak.repository.OddRepository;
import pl.maciejpajak.repository.UserCouponRepository;
import pl.maciejpajak.repository.UserRepository;


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
    private BidAmountBonusRepository bidAmountBonusRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    

    public Collection<CouponShowDto> findAllIndividualCoupons(Long userId) {
        return userCouponRepository.findAllByOwnerIdAndVisible(userId, true).stream()
                .map(DtoMappers.convertUserCouponToDto).collect(Collectors.toList());
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
    
    public Collection<BidAmountBonusDto> findAllBidAmountBonuses() {
        return bidAmountBonusRepository.findAllByVisible(true).stream().map(bonus -> 
                                BidAmountBonusDto.builder()
                                .id(bonus.getId())
                                .minimalBid(bonus.getMinimalBid())
                                .relativeRevenuBonus(bonus.getRelativeRevenuBonus())
                                .build()).collect(Collectors.toList());
    }

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
            groupCoupon.setValue(couponDto.getAmount()); // initial value, this will be increased when coupon inviatation is accepted
            
            groupCoupon.getPlacedBets().forEach(pb -> pb.setCoupon(groupCoupon));
        
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
            userCoupon.setValue(couponDto.getAmount());
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
            // TODO notify user about invitation
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
    
    @Transactional
    public void cancelUnacceptedGroupCoupons(Long gameId) {
        log.debug("canceling unaccepted group coupons");
        groupCouponRepository.findAllByPlacedBetsBetOptionBetGameIdAndStatusAndVisible(gameId, CouponStatus.PENDING, true).forEach(this::cancelGroupCoupon);
    }
    
    private void cancelGroupCoupon(GroupCoupon groupCoupon) {
        log.debug("cancelling coupon with id = {}", groupCoupon.getId());
        transactionService.createTransaction(groupCoupon.getOwnerTransaction().getAmount().negate(), groupCoupon.getOwner(), TransactionType.CANCEL_BET);
        groupCoupon.getIntivations().forEach(inv -> {
            Transaction t = inv.getBetTransaction();
            if (t != null) {
                transactionService.createTransaction(t.getAmount(), t.getOwner(), TransactionType.CANCEL_BET);
            }
            applicationEventPublisher.publishEvent(new NotifyUserEvent(this, inv.getInvitedUser().getId(), groupCoupon.getId(), NotificationType.COUPON_CANCELLED));
        });
        applicationEventPublisher.publishEvent(new NotifyUserEvent(this, groupCoupon.getOwner().getId(), groupCoupon.getId(), NotificationType.COUPON_CANCELLED));
        groupCoupon.setStatus(CouponStatus.CANCELLED);
        groupCouponRepository.save(groupCoupon);
    }
}
