package com.example.bankservice.repositories;

import com.example.bankservice.domains.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>>findAllByUserId(Long userId);

    Optional<Card> findByCardNumber(String cardNumber);

    Optional<Card> findByCvv(String cvv);




}
