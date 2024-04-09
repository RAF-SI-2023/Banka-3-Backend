package com.example.bankservice.repositories;

import com.example.bankservice.domains.model.StockTransaction;
import com.example.bankservice.domains.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {

}
