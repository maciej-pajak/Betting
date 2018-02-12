package pl.maciejpajak.api.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.bet.BetOption;

@Getter
@Setter
@Builder
public class BetDto {

    private Long id;
    private Long gameId;
    private Set<BetOption> betOptions;
    private String description;
    
}
