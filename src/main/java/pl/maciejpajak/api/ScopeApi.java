package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.game.Scope;
import pl.maciejpajak.repository.ScopeRepository;

@RestController
@RequestMapping("/scopes")
public class ScopeApi {
    
    private final ScopeRepository scopeRepository;
    
    @Autowired
    public ScopeApi(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @GetMapping("/all")
    public Collection<Scope> getAllScopes() {
        return scopeRepository.findAllByVisible(true);
    }
    
    @GetMapping("/allForSport")
    public Collection<Scope> getAllScopesBySport(@RequestParam(name = "sportId", required = true) Long sportId) {
        return scopeRepository.findAllByCompetitonsSportIdAndVisible(sportId, true);
    }

}
