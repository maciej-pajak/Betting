package pl.maciejpajak.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;

import lombok.Builder;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.game.util.CouponStatus;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.User;

@Entity
//@Polymorphism(type = PolymorphismType.EXPLICIT)
//@Inheritance(strategy = InheritanceType.JOINED)
////@DiscriminatorColumn(name = "coupon_type")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class UserCoupon extends Coupon {

    public UserCoupon() {
        super();
    }

    @Builder
    private UserCoupon(Long id, boolean visible, LocalDateTime created, Set<PlacedBet> placedBets,
            int unsersolvedBetsCount, User owner, Transaction ownerTransaction, CouponStatus status, BigDecimal value,
            BigDecimal bonus, BigDecimal totalPrize) {
        super(id, visible, created, placedBets, unsersolvedBetsCount, owner, ownerTransaction, status, value, bonus,
                totalPrize);
    }

    
}
