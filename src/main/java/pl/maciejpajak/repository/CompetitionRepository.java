package pl.maciejpajak.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.game.Competition;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    public Collection<Competition> findAllByVisible(boolean isVisible);
    public Collection<Competition> findAllBySportIdAndVisible(Long sportId, boolean isVisible);
    public Collection<Competition> findAllByScopeIdAndVisible(Long scopeId, boolean isVisible);
    public Collection<Competition> findAllBySportIdAndScopeIdAndVisible(Long sportId, Long scopeId, boolean isVisible);
//    public Collection<Compe>
    
}
