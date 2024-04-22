package com.example.emailservice.repository;

import com.example.emailservice.domain.model.TransactionActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionActivationRepository extends JpaRepository<TransactionActivation, Long> {

    Optional<TransactionActivation> findByIdAndActiveIsTrue(Long transactionId);

    Optional<TransactionActivation> findById(Long transactionId);
}
