package pl.maciejpajak.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitionSubscriptionDto extends SubscriptionDto {

    private Long competitionId;
    private String competitionName;
    
    public CompetitionSubscriptionDto() {
        super();
    }

    @Builder
    public CompetitionSubscriptionDto(Long id, Long competitionId, String competitionName) {
        super(id);
        this.competitionId = competitionId;
        this.competitionName = competitionName;
    }

}
