package com.example.emailservice.repository;

import com.example.emailservice.domain.model.EmployeeActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeActivationRepository extends JpaRepository<EmployeeActivation, Long> {

    Optional<EmployeeActivation> findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(String identifier);
}
