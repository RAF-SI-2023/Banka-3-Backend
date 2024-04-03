package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.Forex;

public interface ForexRepository extends JpaRepository<Forex, Long> {
}
