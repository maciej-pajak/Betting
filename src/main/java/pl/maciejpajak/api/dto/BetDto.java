package pl.maciejpajak.api.dto;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetDto {

    @Min(1L)
    private Long oddId;
    
    @Min(1L)
    private Long betOptionId;
    
}
