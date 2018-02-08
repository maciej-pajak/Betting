package pl.maciejpajak.domain.coupon;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import pl.maciejpajak.domain.bet.PlacedBet;
import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.User;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "coupon_type")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    protected LocalDateTime created;
    
    @OneToMany(mappedBy = "coupon")
    protected Set<PlacedBet> placedBets;
    
    @ManyToOne
    private User owner;
    
    @OneToOne
    private Transaction ownerTransaction;
    
    private boolean resolved;
    
}
