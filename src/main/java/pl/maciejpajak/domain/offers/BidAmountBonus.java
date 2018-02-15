package pl.maciejpajak.domain.offers;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BidAmountBonus {

    @Id
    @GeneratedValue
    private Long id;
    
    private boolean visible = true;
    
    private BigDecimal minimalBid;
    
    private BigDecimal relativeRevenuBonus = BigDecimal.ZERO;
    
}
