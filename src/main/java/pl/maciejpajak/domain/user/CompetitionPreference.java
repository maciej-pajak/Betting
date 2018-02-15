package pl.maciejpajak.domain.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.game.Competition;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompetitionPreference extends Preference {

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Builder
    private CompetitionPreference(Long id, User user, double rating, boolean subsribed, Competition competition) {
        super(id, user, rating, subsribed);
        this.competition = competition;
    }

    
}
