package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.game.Competition;
import pl.maciejpajak.repository.CompetitionRepository;

@RestController
@RequestMapping("/competitions")
public class CompetitionApi {
    
    private final CompetitionRepository competitionRepository;
    
    @Autowired
    public CompetitionApi(CompetitionRepository competitonRepository) {
        this.competitionRepository = competitonRepository;
    }

    @GetMapping("/all")
    private Collection<Competition> getAllCompetitions(
            @RequestParam(name = "sportId", required = false) Long sportId,
            @RequestParam(name = "scopeId", required = false) Long scopeId) {
        
        boolean isSportNull = sportId == null;
        boolean isScopeNull = scopeId == null;
        
        if (isSportNull && isScopeNull) {
            return competitionRepository.findAllByVisible(true);
        } else if (isSportNull) {
            return competitionRepository.findAllByScopeIdAndVisible(scopeId, true);
        } else if (isScopeNull) {
            return competitionRepository.findAllBySportIdAndVisible(sportId, true);
        } else {
            return competitionRepository.findAllBySportIdAndScopeIdAndVisible(sportId, scopeId, true);
        }
    }

}
