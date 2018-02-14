package pl.maciejpajak.domain.game.score;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.game.Game;

@Entity
@Getter
@Setter
public class GameScore extends Score {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    public GameScore() {
        super();
    }

    public GameScore(Long id, int partyOneScore, int partyTwoScore, LocalDateTime time, Game game) {
        super(id, partyOneScore, partyTwoScore, time);
        this.game = game;
    }
    
    
    
}
