package com.example.bankservice.repository;

import com.example.bankservice.domain.model.CommissionFromCurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface CommissionFromCurrencyExhangeRepository
        extends JpaRepository<CommissionFromCurrencyExchange, Long> {
    
    @Modifying
    @Transactional
    @Query("UPDATE CommissionFromCurrencyExchange c SET c.amount = c.amount + :amount, c.numberOfTransactions = c.numberOfTransactions + :transactions WHERE c.commisionId = :id")
    void updateCommission(@Param("id") Long id, @Param("amount") BigDecimal amount,
            @Param("transactions") int transactions);
}

