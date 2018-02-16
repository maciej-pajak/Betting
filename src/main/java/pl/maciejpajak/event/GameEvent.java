package pl.maciejpajak.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import pl.maciejpajak.dto.EventDto;

@Getter
public class GameEvent extends ApplicationEvent {

    private EventDto eventDto;
    
    public GameEvent(Object source, EventDto eventDto) {
        super(source);
        this.eventDto = eventDto;
    }

}
