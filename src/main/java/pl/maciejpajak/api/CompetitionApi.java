package pl.maciejpajak.api;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.maciejpajak.domain.game.Competition;
import pl.maciejpajak.service.CompetitionService;

@RestController
@RequestMapping("/competitions")
public class CompetitionApi {
    
    private final CompetitionService competitionService;
    
    @Autowired
    public CompetitionApi(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/all")
    public Collection<Competition> getAllCompetitions(
            @RequestParam(name = "sportId", required = false) Long sportId,
            @RequestParam(name = "scopeId", required = false) Long scopeId) {
        return competitionService.findAllCompetitions(sportId, scopeId);
    }
    
}
