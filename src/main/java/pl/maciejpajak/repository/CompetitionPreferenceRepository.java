package pl.maciejpajak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.user.CompetitionPreference;

public interface CompetitionPreferenceRepository extends JpaRepository<CompetitionPreference, Long>{

    public Optional<CompetitionPreference> findOneByCompetitionIdAndUserId(Long competitionId, Long userId);
    
}
