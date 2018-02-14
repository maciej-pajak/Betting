package pl.maciejpajak.domain.bet;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.coupon.UserCoupon;
import pl.maciejpajak.domain.game.util.BetOptionStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacedBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @JsonIgnore
    private boolean visible;
    
//    @ManyToOne
//    @JoinColumn(name = "bet_id")
//    private Bet bet;
    
    @ManyToOne
    @JoinColumn(name = "bet_option_id")
    private BetOption betOption;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "odd_id")
    private Odd odd;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private UserCoupon coupon;
    
    @Enumerated(EnumType.STRING)
    private BetOptionStatus status;
    
}
