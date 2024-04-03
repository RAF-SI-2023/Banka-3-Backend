package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.Future;


public interface FutureRepository extends JpaRepository<Future, Long> {
}
