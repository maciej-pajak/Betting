package pl.maciejpajak.domain.game;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maciejpajak.domain.user.CompetitionPreference;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @JsonIgnore
    private boolean visible = true;
    
    @NotBlank
    private String name;
    
    @ManyToOne
    private Scope scope;
    
    @ManyToOne
    private Sport sport;
    
    @JsonIgnore
    @OneToMany(mappedBy = "competition")
    private Set<Game> games;
    
    // this is only for jparepository to generate correct find function, can be removed and replaced with custom @Query
    @OneToMany(mappedBy = "competition")  
    @JsonIgnore
    private Set<CompetitionPreference> competitionPreferences;
    
}
