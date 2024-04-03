package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.Ticker;

public interface TickerRepository extends JpaRepository<Ticker, Long> {
    Ticker findByTicker (String ticker);
}
