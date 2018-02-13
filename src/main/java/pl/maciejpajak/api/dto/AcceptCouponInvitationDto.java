package pl.maciejpajak.api.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptCouponInvitationDto {
    
    private Long couponInvitationId;
    private BigDecimal amount;

}
