package rs.edu.raf.exchangeservice.repository.myListingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;

public interface MyStockRepository extends JpaRepository<MyStock, Long> {
    MyStock findByMyStockId(Long id);
    MyStock findByTicker(String ticker);
}
