package pl.maciejpajak.domain.news;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.game.Competition;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompetitionNews extends News {

    @ManyToOne
    @JoinColumn(name = "competion_id")
    private Competition competition;

    @Builder
    private CompetitionNews(Long id, boolean visible, boolean sent, String content, Competition competition) {
        super(id, visible, sent, content);
        this.competition = competition;
    }
    
}
