package pl.maciejpajak.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.game.util.CouponStatus;

@Getter
@Setter
@Builder
public class CouponShowDto {

    private Long id;
    private LocalDateTime created;
    private Set<PlacedBetShowDto> placedBets;
    private Long ownerId;
    private CouponStatus status;
    private BigDecimal value;
    private BigDecimal bonus;
    private BigDecimal totalPrize;
    
}
