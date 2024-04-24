package rs.edu.raf.exchangeservice.repository.myListingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface MyStockRepository extends JpaRepository<MyStock, Long> {
    MyStock findByMyStockId(Long id);
    MyStock findByTicker(String ticker);
}
