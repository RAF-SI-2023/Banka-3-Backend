package com.example.emailservice.repository;

import com.example.emailservice.domain.model.UserActivation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserActivationRepository extends MongoRepository<UserActivation, Long> {

    Optional<UserActivation> findUserActivationByCodeAndActivationPossibleIsTrue(int code);

    Optional<List<UserActivation>> findAllByActivationPossible(boolean bool);
}