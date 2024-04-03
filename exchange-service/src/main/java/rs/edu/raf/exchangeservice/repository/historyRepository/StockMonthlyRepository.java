package rs.edu.raf.exchangeservice.repository.historyRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;

import java.util.List;

public interface StockMonthlyRepository extends JpaRepository<StockMonthly, Long> {
    List<StockMonthly> findByTicker(String ticker);
}
