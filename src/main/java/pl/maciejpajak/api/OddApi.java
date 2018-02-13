package pl.maciejpajak.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.exception.BaseEntityNotFoundException;
import pl.maciejpajak.repository.OddRepository;

@RestController
@RequestMapping("/odds")
public class OddApi {
    
    private final OddRepository oddRepository;
    
    @Autowired
    public OddApi(OddRepository oddRepository) {
        this.oddRepository = oddRepository;
    }

    @GetMapping("/{betOptionId}")
    public Odd getNewestOddByBetId(@PathVariable(name = "betOptionId", required = true) Long betOptionId) {
        return oddRepository.findFirstByBetOptionIdOrderByCreatedDesc(betOptionId).orElseThrow(() -> new BaseEntityNotFoundException(betOptionId));
    }
    
}
