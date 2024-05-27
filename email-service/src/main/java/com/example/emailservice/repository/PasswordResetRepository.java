package com.example.emailservice.repository;

import com.example.emailservice.domain.model.PasswordReset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends MongoRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByIdentifier(String identifier);

}
