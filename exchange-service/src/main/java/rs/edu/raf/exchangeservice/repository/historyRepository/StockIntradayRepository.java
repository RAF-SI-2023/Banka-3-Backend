package rs.edu.raf.exchangeservice.repository.historyRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;

import java.util.List;

public interface StockIntradayRepository extends JpaRepository<StockIntraday, Long> {
    List<StockIntraday> findByTicker(String ticker);
}
