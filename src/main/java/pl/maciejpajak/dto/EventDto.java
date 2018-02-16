package pl.maciejpajak.dto;

import java.time.LocalDateTime;

import lombok.Data;
import pl.maciejpajak.domain.util.EventType;

@Data
public class EventDto {
    
    private Long gameId;
    
    private EventType eventType;
    
    private String message;
    
    private int value;
    
    private LocalDateTime time;

}
