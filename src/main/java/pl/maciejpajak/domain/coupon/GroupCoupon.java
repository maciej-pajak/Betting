package pl.maciejpajak.domain.coupon;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

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
    
    public GroupCoupon() {}
    
    public GroupCoupon(LocalDateTime created, Set<PlacedBet> placedBets, User owner, Transaction ownerTransaction) {
        super();
        this.created = created;
        this.placedBets = placedBets;
        this.owner = owner;
        this.ownerTransaction = ownerTransaction;
        this.status = CouponStatus.UNRESOLVED;
        this.visible = true;
    }
    
}
