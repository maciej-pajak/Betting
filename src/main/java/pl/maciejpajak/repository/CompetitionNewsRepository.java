package pl.maciejpajak.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.news.CompetitionNews;

public interface CompetitionNewsRepository extends JpaRepository<CompetitionNews, Long>{

    public Collection<CompetitionNews> findAllByCompetitionIdAndSent(Long competitionId, boolean isSent);
    
    public Collection<CompetitionNews> findAllBySentAndCompetitionCompetitionPreferencesUserIdAndCompetitionCompetitionPreferencesSubscribed(boolean isSent, Long userId, boolean isSubscribed);

    public Collection<CompetitionNews> findAllBySent(boolean isSent);
    
}

