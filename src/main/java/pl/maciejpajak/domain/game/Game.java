package pl.maciejpajak.domain.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.bet.Odd;
import pl.maciejpajak.domain.game.util.GameStatus;

@Entity
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    private boolean visible = true;
    
    private LocalDateTime startTime;
    
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    private String description;
    
    @ManyToOne
    private Competition competition;
    
    @ManyToOne
    @JoinColumn(name = "party_one_id")
    private PlayingParty partyOne;
    
    @ManyToOne
    @JoinColumn(name = "party_two_id")
    private PlayingParty partyTwo;
    
    @OneToMany(mappedBy = "game")
    private Set<GamePart> gameParts;
    
}
