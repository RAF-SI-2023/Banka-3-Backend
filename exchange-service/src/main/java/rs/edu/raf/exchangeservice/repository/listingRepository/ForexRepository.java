package rs.edu.raf.exchangeservice.repository.listingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;

public interface ForexRepository extends JpaRepository<Forex, Long> {
}
