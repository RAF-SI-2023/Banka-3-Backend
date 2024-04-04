package rs.edu.raf.exchangeservice.repository.listingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;

public interface TickerRepository extends JpaRepository<Ticker, Long> {
    Ticker findByTicker(String ticker);
}
