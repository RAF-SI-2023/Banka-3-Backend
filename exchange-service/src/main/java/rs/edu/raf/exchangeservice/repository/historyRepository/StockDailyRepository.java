package rs.edu.raf.exchangeservice.repository.historyRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;


public interface StockDailyRepository extends JpaRepository<StockDaily, Long> {
}
