package com.example.bankservice.repository;

import com.example.bankservice.domain.model.Transaction;
import com.example.bankservice.domain.model.enums.TransactionStatus;
import com.example.bankservice.domain.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findAllByTransactionStatus(TransactionStatus transactionStatus);

    Optional<List<Transaction>> findAllByType(TransactionType transactionType);

    Optional<List<Transaction>> findByAccountFromOrAccountToAndType(String accountFrom, String accountTo, TransactionType transactionType);

}
