package com.example.bankservice.repository;

import com.example.bankservice.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>> findAllByAccountNumber(String accountNumber);

    Optional<Card> findByAccountNumber(String accountNumber);

    Optional<Card> findByCardNumber(String cardNumber);

}
