package pl.maciejpajak.api.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponDto {
    
    @DecimalMin(value = "1.00")
    private BigDecimal amount;
    
    @NotEmpty
    @Valid  // TODO check this
    private List<BetDto> bets;
    
    private boolean oddsChangeAccepted;

}
