package pl.maciejpajak.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Wallet wallet;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime operationTime;
    
    private BigDecimal amount;
    
    @ManyToOne
    @JoinColumn(name = "type_id")
    private TransactionType type;

}
