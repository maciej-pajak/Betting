package pl.maciejpajak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.maciejpajak.domain.user.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
