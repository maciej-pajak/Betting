package pl.maciejpajak.domain.offers;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class BidAmountDiscount {

    @Id
    @GeneratedValue
    private Long id;
    
    private boolean visible = true;
    
    private BigDecimal minimalAmount;
    
    private BigDecimal percentageRevenuBonus;
    
}
