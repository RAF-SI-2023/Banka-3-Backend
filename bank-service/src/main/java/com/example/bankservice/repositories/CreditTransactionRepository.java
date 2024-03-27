package com.example.bankservice.repositories;

import com.example.bankservice.domains.model.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

}
