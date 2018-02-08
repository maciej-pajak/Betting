package pl.maciejpajak.domain.user;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(optional = false)
    @PrimaryKeyJoinColumn
    private User user;
    
    private BigDecimal balance;
    
    @OneToMany(mappedBy = "wallet")
    private Set<Transaction> transactions;
  
}
