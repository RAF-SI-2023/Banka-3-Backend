package com.example.emailservice.repository;

import com.example.emailservice.domain.model.CompanyActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyActivationRepository extends JpaRepository<CompanyActivation, Long> {

    Optional<CompanyActivation> findCompanyActivationByCodeAndActivationPossibleIsTrue(int code);

    Optional<List<CompanyActivation>> findAllByActivationPossible(boolean bool);
}
