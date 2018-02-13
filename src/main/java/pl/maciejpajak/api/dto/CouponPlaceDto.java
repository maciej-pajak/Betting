package pl.maciejpajak.api.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponPlaceDto {
    
    @DecimalMin(value = "1.00")
    private BigDecimal amount;
    
    @NotEmpty
    private List<BetOptionWithOddDto> betOptionsWithOdds;
    
    private boolean oddsChangeAccepted;

}
