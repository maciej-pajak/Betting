package pl.maciejpajak.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptCouponInvitationDto {
    
    @Min(value = 1L)
    private Long couponInvitationId;
    
    @DecimalMin(value = "1.00")
    private BigDecimal amount;

}
