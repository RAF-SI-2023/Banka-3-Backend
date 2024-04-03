package rs.edu.raf.exchangeservice.repository.myListingsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.myListings.MyStock;

public interface MyStockRepository extends JpaRepository<MyStock, Long> {
    MyStock findByMyStockId(Long id);
    MyStock findByTicker(String ticker);
}
