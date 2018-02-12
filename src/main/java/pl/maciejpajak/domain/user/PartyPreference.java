package pl.maciejpajak.domain.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.maciejpajak.domain.game.PlayingParty;

@Entity
public class PartyPreference extends Preference {

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PlayingParty playingParty;
    
}
