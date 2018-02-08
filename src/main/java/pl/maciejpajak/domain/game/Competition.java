package pl.maciejpajak.domain.game;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @NotBlank
    private String name;
    
    @ManyToOne
    private GeoScope scope;
    
    @ManyToOne
    private Sport sport;
    
    @OneToMany(mappedBy = "competition")
    private Set<Game> games;
    
}
