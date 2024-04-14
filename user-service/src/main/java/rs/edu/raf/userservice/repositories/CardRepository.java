package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.userservice.domains.model.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>>findAllByUserId(Long userId);

    Optional<Card> findByCardNumber(String cardNumber);

    Optional<Card> findByAccountNumber(String accountNumber);

    Optional<Card> findByCvv(String cvv);




}
