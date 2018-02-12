package pl.maciejpajak.domain.bet;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BetOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @JsonIgnore
    private boolean visible =  true;
    
    @JsonIgnore
    private String winCondition;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Bet bet;
    
//    @JsonIgnore
//    @OneToMany(mappedBy = "betOption")
//    private Set<Odd> odds;
    
    private String description;
}
