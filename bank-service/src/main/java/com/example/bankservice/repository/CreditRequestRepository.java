package com.example.bankservice.repository;

import com.example.bankservice.domain.model.CreditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}
