package pl.maciejpajak.domain.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.domain.util.CouponStatus;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Coupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected boolean visible;

    protected LocalDateTime created;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.PERSIST)
    protected Set<PlacedBet> placedBets;
    
    protected int unsersolvedBetsCount;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    protected User owner;

    @OneToOne
    protected Transaction ownerTransaction;

    @Enumerated(EnumType.STRING)
    protected CouponStatus status;
    
    protected BigDecimal value;
    
    protected BigDecimal bonus;
    
    protected BigDecimal totalPrize;

}
