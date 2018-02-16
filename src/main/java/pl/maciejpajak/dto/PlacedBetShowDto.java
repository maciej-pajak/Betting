package pl.maciejpajak.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlacedBetShowDto {
    
    private Long id;
    private Long betOptionId;
    private String betOptionDescription;
    private Long oddId;
    private BigDecimal oddValue;
    
}
