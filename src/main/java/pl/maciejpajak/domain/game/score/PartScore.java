package pl.maciejpajak.domain.game.score;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.game.GamePart;

@Entity
@Getter
@Setter
public class PartScore extends Score {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_part_id")
    @JsonIgnore
    private GamePart gamePart;

    public PartScore(Long id, int partyOneScore, int partyTwoScore, LocalDateTime time, GamePart gamePart) {
        super(id, partyOneScore, partyTwoScore, time);
        this.gamePart = gamePart;
    }
    
    
    
}
