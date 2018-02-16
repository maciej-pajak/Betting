package pl.maciejpajak.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInvitationShowDto {
    
    private Long invitedUserId;
    private LocalDateTime userTransactionTime;
    private BigDecimal userTransactoinAmount;

}
