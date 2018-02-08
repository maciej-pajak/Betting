package pl.maciejpajak.domain.bet;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import pl.maciejpajak.domain.game.Game;

@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @ManyToOne
    private Game game;
    
    @ManyToOne
    private BetCategory category;
    
    @OneToMany(mappedBy = "bet")
    private Set<BetOption> betOptions;
    
    private String description;
    
}
