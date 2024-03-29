package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.Credit;

import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {

    Optional<List<Credit>> findByUser_UserId(Long userId);
}
