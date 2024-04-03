package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
