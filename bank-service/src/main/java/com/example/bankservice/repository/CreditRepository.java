package com.example.bankservice.repository;

import com.example.bankservice.domain.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    Optional<List<Credit>> findAllByUserId(Long userId);
}
