package com.example.emailservice.repository;

import com.example.emailservice.model.UserActivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserActivationRepository extends JpaRepository<UserActivation,Long>{

    Optional<UserActivation> findUserActivationByCodeAndActivationPossibleIsTrue(int code);
}