package pl.maciejpajak.testing.event.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import pl.maciejpajak.testing.event.EventDto;

@Getter
public class GameEndEvent extends GameEvent {

  //TODO serial version UID
    public GameEndEvent(Object source, EventDto eventDto) {
        super(source, eventDto);
    }

    
}
