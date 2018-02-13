package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.api.dto.BetDto;
import pl.maciejpajak.api.temp.BetService;

@RestController
@RequestMapping("/bets")
public class BetApi {
    
    private final BetService betService;
    
    @Autowired
    public BetApi(BetService betService) {
        this.betService = betService;
    }
    
    @GetMapping("/{betId}")
    private BetDto getBetById(@PathVariable(name = "betId", required = true) Long betId) {
        return betService.findOneById(betId);
    }

    @GetMapping("/by-game/{gameId}")
    private Collection<BetDto> getBetsByGame(@PathVariable(name = "gameId", required = true) Long gameId) {
        return betService.findAllByGameIdAndVisible(gameId);
    }
    
}
