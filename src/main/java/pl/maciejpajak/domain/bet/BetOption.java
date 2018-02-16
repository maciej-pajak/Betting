package pl.maciejpajak.domain.bet;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import pl.maciejpajak.domain.util.BetOptionStatus;

@Entity
@Getter
@Setter
public class BetOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
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
    
    @Enumerated(EnumType.STRING)
    private BetOptionStatus status;
    
}
