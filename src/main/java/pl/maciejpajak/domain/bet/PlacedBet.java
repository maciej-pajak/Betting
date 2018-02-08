package pl.maciejpajak.domain.bet;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import pl.maciejpajak.domain.coupon.Coupon;

@Entity
public class PlacedBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @OneToOne
    @JoinColumn(name = "odd_id")
    private Odd odd;
    
    private BigDecimal amount; // TODO
    
    @ManyToOne
    private Coupon coupon;
    
}
