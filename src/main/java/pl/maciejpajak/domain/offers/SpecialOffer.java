package pl.maciejpajak.domain.offers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.maciejpajak.domain.user.User;

@Entity
public class SpecialOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private boolean archived = false;
    
    private LocalDateTime endDate;
    
    private boolean valid;
    
    private BigDecimal bonus;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
