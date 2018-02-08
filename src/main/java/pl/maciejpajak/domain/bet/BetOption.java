package pl.maciejpajak.domain.bet;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class BetOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    private String winCondition;
    
    @ManyToOne
    private Bet bet;
    
    @OneToMany(mappedBy = "betOption")
    private Set<Odd> odds;
    
    private String description;
}
