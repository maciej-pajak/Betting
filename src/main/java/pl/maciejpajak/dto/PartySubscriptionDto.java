package pl.maciejpajak.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartySubscriptionDto extends SubscriptionDto {

    private Long partyId;
    private String partyName;
    
    public PartySubscriptionDto() {
        super();
    }

    @Builder
    public PartySubscriptionDto(Long id, Long partyId, String partyName) {
        super(id);
        this.partyId = partyId;
        this.partyName = partyName;
    }

}
