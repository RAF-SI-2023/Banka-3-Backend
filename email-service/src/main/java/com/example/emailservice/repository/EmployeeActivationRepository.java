package com.example.emailservice.repository;

import com.example.emailservice.model.EmployeeActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/***
 *Pretraga u bazi po identifier-u i ako je activation possible
 *
 */
@Repository
public interface EmployeeActivationRepository extends JpaRepository<EmployeeActivation, Long> {
    Optional<EmployeeActivation> findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(String identifier);

}
