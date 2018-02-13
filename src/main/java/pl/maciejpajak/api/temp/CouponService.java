package pl.maciejpajak.api.temp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.api.dto.BetOptionWithOddDto;
import pl.maciejpajak.api.dto.PlacedCouponDto;
import pl.maciejpajak.api.dto.PlacedGroupCouponDto;
import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.coupon.CouponInvitation;
import pl.maciejpajak.domain.coupon.GroupCoupon;
import pl.maciejpajak.domain.coupon.UserCoupon;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.exception.BetsCombinationNotAllowedException;
import pl.maciejpajak.exception.InsufficientFundsException;
import pl.maciejpajak.exception.OddHasChangedException;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.CouponRepository;
import pl.maciejpajak.repository.GroupCouponRepository;
import pl.maciejpajak.repository.OddRepository;
import pl.maciejpajak.repository.TransactionRepository;
import pl.maciejpajak.repository.UserRepository;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final GroupCouponRepository groupCouponRepository;
    private final OddRepository oddRepository;
    private final BetOptionRepository betOptionRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    
    @Autowired
    public CouponService(CouponRepository couponRepository,
            OddRepository oddRepository,
            BetOptionRepository betOptionRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            GroupCouponRepository groupCouponRepository) {
        this.couponRepository = couponRepository;
        this.oddRepository = oddRepository;
        this.betOptionRepository = betOptionRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.groupCouponRepository = groupCouponRepository;
    }
    
    public Collection<UserCoupon> findAllForCurrentUser() {
        Long userId = 1L; // TODO change to current user
        return couponRepository.findAllByOwnerIdAndVisible(userId, true);
    }
    
    @Transactional
    public void createCoupon(PlacedCouponDto couponDto) {
        Long userId = 1L; // TODO change to current user
        User user = userRepository.findOneById(userId).orElseThrow(() -> new BaseEntityNotFoundException(userId));

        GroupCoupon coupon = new GroupCoupon(LocalDateTime.now(),
                prepareAndValidateBets(couponDto),
                user,
                createTransaction(couponDto.getAmount(), user));
        
        coupon.getPlacedBets().forEach(pb -> pb.setCoupon(coupon));
        
        if (couponDto instanceof PlacedGroupCouponDto) {
            sendInvitations(coupon, (PlacedGroupCouponDto) couponDto);
            groupCouponRepository.saveAndFlush(coupon);
        } else {
            couponRepository.saveAndFlush((UserCoupon) coupon);
        }
    }

    private void sendInvitations(GroupCoupon coupon, PlacedGroupCouponDto groupCouponDto) {
        List<Long> invitedIds = groupCouponDto.getInvitedUsersIds();
        if (invitedIds.size() == 0) {
            throw new RuntimeException(); // TODO define exception
        }
        Set<CouponInvitation> invitations = new HashSet<>();
        // TODO handle duplicated ids
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
        groupCouponRepository.saveAndFlush(coupon);
    }
       
    private Transaction createTransaction(BigDecimal amount, User user) {
        if(user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        
        Transaction transaction = 
                Transaction.builder()
                    .amount(amount)
                    .operationTime(LocalDateTime.now())
                    .owner(user)
                    .visible(true)
//                    .type(type)   // TODO add transactoinType
                    .build();
        
        // save transaction
        transactionRepository.saveAndFlush(transaction);
        // set user balance
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.saveAndFlush(user);
        return transaction;
    }
    
    private Set<PlacedBet> prepareAndValidateBets(PlacedCouponDto couponDto) {
        Set<PlacedBet> placedBets = new HashSet<>();
        
        // prepare single bets list with odds
        for (BetOptionWithOddDto b : couponDto.getBetOptionsWithOdds()) {
            BetOption betOption = betOptionRepository.findOneByIdAndVisible(b.getBetOptionId(), true)
                                    .orElseThrow(() -> new BaseEntityNotFoundException(b.getBetOptionId()));
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
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX about ot get id");
            Long id = pb.getBetOption().getBet().getId();
            if (betsIds.contains(id)) {
                throw new BetsCombinationNotAllowedException(); // TODO add message
            } else {
                betsIds.add(id);
            }
        });
        
        return placedBets;
    }
    
    public void acceptCouponInvitation(Long userId, Long invitationId, BigDecimal amou) {
        User user = userRepository.findOneByIdAndVisible(userId, true).orElseThrow(() -> new BaseEntityNotFoundException(userId));
        
    }
}
