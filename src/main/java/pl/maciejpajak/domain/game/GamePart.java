package pl.maciejpajak.domain.game;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.game.score.PartScore;
import pl.maciejpajak.domain.game.util.GameResult;
import pl.maciejpajak.domain.game.util.GameStatus;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    private boolean visible;
    
    @ManyToOne
    @JsonIgnore
    private Game game;
    
    private LocalDateTime startTime;
    
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    @Enumerated(EnumType.STRING)
    private GameResult result;
    
    private Duration duration;
    
    @OneToOne
    private PartScore finalPartScore;
    
}
