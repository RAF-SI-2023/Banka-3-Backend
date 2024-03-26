package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.CreditRequest;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {
}
