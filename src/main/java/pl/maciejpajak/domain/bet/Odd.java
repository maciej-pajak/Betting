package pl.maciejpajak.domain.bet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.maciejpajak.domain.game.Game;

@Entity
public class Odd {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @JsonIgnore
    @ManyToOne
    private BetOption betOption;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    
    @JsonIgnore
    private LocalDateTime created;
    
    private BigDecimal value;

}
