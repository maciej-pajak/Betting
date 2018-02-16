package pl.maciejpajak.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.util.CouponStatus;

@Getter
@Setter
@Builder
public class CouponShowDto {

    private Long id;
    private LocalDateTime created;
    private Set<PlacedBetShowDto> placedBets;
    private Long ownerId;
    private BigDecimal ownerTransactionAmount;
    private CouponStatus status;
    private Set<CouponInvitationShowDto> invitations;
    private BigDecimal value;
    private BigDecimal bonus;
    private BigDecimal totalPrize;
    
}
