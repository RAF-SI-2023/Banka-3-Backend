package rs.edu.raf.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.userservice.domains.model.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {

}
