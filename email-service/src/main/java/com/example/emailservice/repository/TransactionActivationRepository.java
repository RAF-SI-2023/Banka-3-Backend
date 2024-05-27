package com.example.emailservice.repository;

import com.example.emailservice.domain.model.TransactionActivation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionActivationRepository extends MongoRepository<TransactionActivation, Long> {

    Optional<TransactionActivation> findByCodeAndActiveIsTrue(int code);

    Optional<TransactionActivation> findById(Long transactionId);
}
