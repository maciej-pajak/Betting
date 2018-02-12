package pl.maciejpajak.domain.news;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.maciejpajak.domain.game.PlayingParty;

@Entity
public class PartyNews extends News {

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PlayingParty palyingParty;
    
}
