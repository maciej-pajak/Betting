package pl.maciejpajak.domain.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.game.PlayingParty;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PartyPreference extends Preference {

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PlayingParty playingParty;

    @Builder
    private PartyPreference(Long id, User user, double rating, boolean subsribed, PlayingParty playingParty) {
        super(id, user, rating, subsribed);
        this.playingParty = playingParty;
    }
    
}
