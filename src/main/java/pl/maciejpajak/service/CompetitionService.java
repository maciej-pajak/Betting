package pl.maciejpajak.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.game.Competition;
import pl.maciejpajak.repository.CompetitionRepository;

@Service
public class CompetitionService {
    
    private final CompetitionRepository competitionRepository;
    
    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public Collection<Competition> findAllCompetitions(Long sportId, Long scopeId) {
        
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
