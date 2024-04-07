package rs.edu.raf.exchangeservice.repository.listingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;

import java.util.Optional;


public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByStockId(Long id);
    Optional<Stock> findByTicker(String ticker);
}
