package pl.maciejpajak.domain.bet;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import pl.maciejpajak.domain.game.Game;

@Entity
public class Odd {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @ManyToOne
    private BetOption betOption;
    
    @ManyToOne
    private Game game;
    
    private LocalDateTime created;

}
