package pl.maciejpajak.domain.game;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import pl.maciejpajak.domain.bet.Odd;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime startTime;
    
    private byte status; // TODO maybe enum
    
    @OneToMany(mappedBy = "game")
    private List<GamePart> parts;
    
    @ManyToOne
    @JoinColumn(name = "party_one_id")
    private PlayingParty partyOne;
    
    @ManyToOne
    @JoinColumn(name = "party_two_id")
    private PlayingParty partyTwo;
    
    @OneToMany(mappedBy = "game")
    private Set<Odd> odds;
    
    @OneToMany(mappedBy = "game")
    private Set<GamePart> gameParts;
    
    @ManyToOne
    private Competition competition;
    
}
