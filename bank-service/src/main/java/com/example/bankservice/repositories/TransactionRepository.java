package com.example.bankservice.repositories;

import com.example.bankservice.domains.model.Transaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.accountFrom = :accountFrom")
    Optional<List<Transaction>> findAllTransactionsByAccountFrom(String accountFrom);

    @Query("SELECT t FROM Transaction t WHERE t.accountTo = :accountTo")
    Optional<List<Transaction>> findAllTransactionsByAccountTo(String accountTo);

    Optional<List<Transaction>> findByState(TransactionState state);

}
