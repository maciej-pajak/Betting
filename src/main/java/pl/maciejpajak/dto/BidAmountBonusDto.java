package pl.maciejpajak.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BidAmountBonusDto {

    private Long id;
    private BigDecimal minimalBid;
    private BigDecimal relativeRevenuBonus;
    
}
