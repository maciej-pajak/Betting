package pl.maciejpajak.domain.game;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GamePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    private boolean visible;
    
    @ManyToOne
    private Game game;
    
    private LocalDateTime startTime;
    
    private boolean timeable;
    
    private byte status; // TODO maybe enum
    
    private int length;
    
    private int partyOneScore;
    private int partyTwoScore;
    
}
