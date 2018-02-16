package pl.maciejpajak.domain.news;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.game.PlayingParty;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PartyNews extends News {

    @ManyToOne
    @JoinColumn(name = "party_id")
    private PlayingParty palyingParty;

    @Builder
    private PartyNews(Long id, boolean visible, boolean sent, String content, PlayingParty palyingParty) {
        super(id, visible, sent, content);
        this.palyingParty = palyingParty;
    }
    
}
