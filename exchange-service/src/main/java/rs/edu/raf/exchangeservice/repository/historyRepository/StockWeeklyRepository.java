package rs.edu.raf.exchangeservice.repository.historyRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.history.StockWeekly;

import java.util.List;

public interface StockWeeklyRepository extends JpaRepository<StockWeekly, Long> {
    List<StockWeekly> findByTicker(String ticker);
}
