package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.Stock;


public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByStockId(Long id);
}
