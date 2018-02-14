package pl.maciejpajak.testing.event.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import pl.maciejpajak.testing.event.EventDto;

@Getter
public abstract class GameEvent extends ApplicationEvent {

    private EventDto eventDto;
    
    public GameEvent(Object source, EventDto eventDto) {
        super(source);
        this.eventDto = eventDto;
    }

}
