package rs.edu.raf.exchangeservice.repository.listingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.listing.Bank4Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;

public interface Bank4StockRepository extends JpaRepository<Bank4Stock, Long> {

}
