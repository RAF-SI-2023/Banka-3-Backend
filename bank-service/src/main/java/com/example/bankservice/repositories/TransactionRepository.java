package com.example.bankservice.repositories;

import com.example.bankservice.domains.model.Transaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByAccountFrom(Long accountFromId);
    Optional<List<Transaction>> findByAccountTo(Long accountToId);
    Optional<List<Transaction>> findByState(TransactionState state);
}
