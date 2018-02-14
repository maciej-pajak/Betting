package pl.maciejpajak.testing.event.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import pl.maciejpajak.testing.event.EventDto;

@Getter
public class GameStartEvent extends GameEvent {

    public GameStartEvent(Object source, EventDto eventDto) {
        super(source, eventDto);
    }
//TODO serial version UID
   
    

}
