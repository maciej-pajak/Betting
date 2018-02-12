package pl.maciejpajak.domain.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.maciejpajak.domain.game.Competition;

@Entity
public class CompetitionPreference extends Preference {

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

}
