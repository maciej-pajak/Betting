package pl.maciejpajak.testing.event;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.game.util.EventType;

@Data
public class EventDto {
    
    private Long gameId;
    
    private EventType eventType;
    
    private String message;
    
    private int value;
    
    private LocalDateTime time;

}
