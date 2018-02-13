package pl.maciejpajak.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponDto {

    private Long id;
    private LocalDateTime created;
    private Long ownerId;
    private BigDecimal ownerAmount;
    private boolean resolved;
}
