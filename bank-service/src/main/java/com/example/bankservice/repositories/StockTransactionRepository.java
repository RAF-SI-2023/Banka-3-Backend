package com.example.bankservice.repositories;

import com.example.bankservice.domains.model.StockTransaction;
import com.example.bankservice.domains.model.enums.TransactionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {

    List<StockTransaction> findByState (TransactionState transactionState);
}
