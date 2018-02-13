package pl.maciejpajak.domain.game;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.game.util.GameStatus;

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
    
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    private int length;
    
    @OneToOne
    private PartScore finalPartScore;
    
}
