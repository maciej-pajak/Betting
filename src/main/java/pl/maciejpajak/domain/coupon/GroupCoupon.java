package pl.maciejpajak.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.game.util.CouponStatus;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.User;

@Entity
@Getter
@Setter
public class GroupCoupon extends Coupon {
    
    @OneToMany(mappedBy = "groupCoupon", cascade = CascadeType.PERSIST)
    private Set<CouponInvitation> intivations;
    
    private int unacceptedInvitationsCount;
    
    public GroupCoupon() {}

    @Builder
    private GroupCoupon(Long id, boolean visible, LocalDateTime created, Set<PlacedBet> placedBets,
            int unsersolvedBetsCount, User owner, Transaction ownerTransaction, CouponStatus status, BigDecimal value,
            BigDecimal bonus, BigDecimal totalPrize, Set<CouponInvitation> intivations, int unacceptedInvitationsCount) {
        super(id, visible, created, placedBets, unsersolvedBetsCount, owner, ownerTransaction, status, value, bonus,
                totalPrize);
        this.intivations = intivations;
        this.unacceptedInvitationsCount = unacceptedInvitationsCount;
    }
    
    
    
}
