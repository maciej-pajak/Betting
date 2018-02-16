package pl.maciejpajak.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.maciejpajak.domain.user.Transaction;
import pl.maciejpajak.domain.user.TransactionType;
import pl.maciejpajak.domain.user.User;
import pl.maciejpajak.exception.InsufficientFundsException;
import pl.maciejpajak.repository.TransactionRepository;
import pl.maciejpajak.repository.UserRepository;

@Service
@Transactional
public class TransactionService {
    
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        super();
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Transaction createTransaction(BigDecimal amount, User user, TransactionType type) {
        
        if(user.getBalance().add(type.addSign(amount)).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }
        
        Transaction transaction = 
                Transaction.builder()
                    .amount(type.addSign(amount))
                    .operationTime(LocalDateTime.now())
                    .owner(user)
                    .visible(true)
                    .type(type)
                    .build();
        log.debug("amount: {}", amount);
        log.debug("creating transaction: {}", transaction);
        log.debug("amount: {}", transaction.getAmount());
        // save transaction
        transactionRepository.saveAndFlush(transaction);
        // set user balance
        user.setBalance(user.getBalance().add(type.addSign(amount)));
        userRepository.saveAndFlush(user);
        return transaction;
    }

}
