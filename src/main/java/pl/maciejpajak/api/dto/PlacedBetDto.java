package pl.maciejpajak.api.dto;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.domain.coupon.UserCoupon;

public class PlacedBetDto { // TODO
    
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bet_option_id")
    private BetOption betOption;
    
    @ManyToOne
    @JoinColumn(name = "odd_id")
    private Odd odd;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private UserCoupon coupon;

}
