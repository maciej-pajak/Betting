package pl.maciejpajak.domain.news;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.maciejpajak.domain.game.Competition;

@Entity
public class CompetitionNews extends News {

    @ManyToOne
    @JoinColumn(name = "competion_id")
    private Competition competition;
    
}
