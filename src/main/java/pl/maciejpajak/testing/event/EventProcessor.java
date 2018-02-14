package pl.maciejpajak.testing.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import pl.maciejpajak.domain.game.Event;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.EventRepository;
import pl.maciejpajak.repository.GameRepository;
import pl.maciejpajak.testing.event.event.GameEndEvent;
import pl.maciejpajak.testing.event.event.GamePartEndEvent;
import pl.maciejpajak.testing.event.event.GamePartStartEvent;
import pl.maciejpajak.testing.event.event.GameStartEvent;
import pl.maciejpajak.testing.event.event.PartyOneScoreEvent;
import pl.maciejpajak.testing.event.event.PartyTwoScoreEvent;

public class EventProcessor {
    
    
    private static final Logger log = LoggerFactory.getLogger(EventProcessor.class);

    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    public void process(EventDto event) {
        log.debug("System received new event {0}", event);
        saveEvent(event);
        
        switch (event.getEventType()) {
        case GAME_START:
            applicationEventPublisher.publishEvent(new GameStartEvent(this, event));
            break;
        case GAME_END:
            applicationEventPublisher.publishEvent(new GameEndEvent(this, event));
            break;
        case GAME_PART_END:
            applicationEventPublisher.publishEvent(new GamePartEndEvent(this, event));
            break;
        case GAME_PART_START:
            applicationEventPublisher.publishEvent(new GamePartStartEvent(this, event));
            break;
        case PARTY_ONE_SCORED:
            applicationEventPublisher.publishEvent(new PartyOneScoreEvent(this, event));
            break;
        case PARTY_TWO_SCORED:
            applicationEventPublisher.publishEvent(new PartyTwoScoreEvent(this, event));
            break;
        }
    }
    
    private void saveEvent(EventDto eventDto) {
        eventRepository.save(
                Event.builder()
                .game(gameRepository.findOneByIdAndVisible(eventDto.getGameId(), true)
                        .orElseThrow(() -> new BaseEntityNotFoundException(eventDto.getGameId())))
                .message(eventDto.getMessage())
                .time(eventDto.getTime())
                .value(eventDto.getValue())
                .visible(true)
                .eventType(eventDto.getEventType())
                .build()             
                );
    }

}
