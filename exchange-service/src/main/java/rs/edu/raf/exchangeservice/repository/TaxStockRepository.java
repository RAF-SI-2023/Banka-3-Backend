package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;

public interface TaxStockRepository extends JpaRepository<TaxStock, Long> {
}
