package pl.maciejpajak.api;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.domain.bet.BetOption;
import pl.maciejpajak.domain.game.util.BetLastCall;
import pl.maciejpajak.domain.game.util.EventType;
import pl.maciejpajak.repository.BetOptionRepository;
import pl.maciejpajak.repository.BetRepository;
import pl.maciejpajak.testing.event.EventDto;
import pl.maciejpajak.testing.event.EventProcessor;

/**
 * This controller is responsible for receiving live events.
 * For demonstration purposes only. 
 * @author mac
 *
 */
@RestController
@RequestMapping("/demo")
public class EventReceiveRestController {
    
    @Autowired
    private EventProcessor eventProcessor;

    @PostMapping("/feed-event")
    @ResponseStatus(value = HttpStatus.OK)
    public void feedEvent(@RequestBody EventDto eventDto) {
        eventProcessor.process(eventDto);
    }
    
    @GetMapping("/show-example")
    public EventDto show() {
        EventDto ev = new EventDto();
        ev.setEventType(EventType.GAME_START);
        ev.setGameId(1L);
        ev.setMessage("message");
        ev.setTime(LocalDateTime.now());
        ev.setValue(3);
        return ev;
    }
    
//    @Autowired
//    private BetRepository betRepository;
//    
//    @Autowired
//    private BetOptionRepository betOptionRepository;
//    
//    @GetMapping("/test")
//    public Collection<BetOption> test() {
//        Collection<Bet> bets = betRepository.findAllByGameIdAndLastCallAndVisible(2L, BetLastCall.GAME_END, true);
//        Collection<BetOption> betOptions = betOptionRepository.findAllByBetInAndVisible(bets, true);
//        return betOptions;
//    }
    
}
