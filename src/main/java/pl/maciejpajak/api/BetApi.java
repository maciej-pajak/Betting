package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.bet.Bet;
import pl.maciejpajak.repository.BetRepository;

@RestController
@RequestMapping("/bets")
public class BetApi {
    
    private final BetRepository betRepository;
    
    @Autowired
    public BetApi(BetRepository betRepository) {
        this.betRepository = betRepository;
    }
    
    @GetMapping("/{betId}")
    private Bet getBetById(@PathVariable(name = "betId", required = true) Long betId) throws Exception {
        // TODO exception
        return betRepository.findOneByIdAndVisible(betId, true).orElseThrow(() -> new Exception());
    }

    @GetMapping("/byGame/{gameId}")
    private Collection<Bet> getBetsByGame(@PathVariable(name = "gameId", required = true) Long gameId) {
        return betRepository.findAllByGameIdAndVisible(gameId, true);
    }
    
}
