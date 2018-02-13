package pl.maciejpajak.domain.game;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.game.util.EventType;

@Entity
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    private boolean visible;
    
    @ManyToOne
    private Game game;
    
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    private String message;
    
}
