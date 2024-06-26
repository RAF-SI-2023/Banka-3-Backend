package rs.edu.raf.exchangeservice.repository.myListingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyMarginStock;

import java.util.List;

public interface MyMarginStockRepository extends JpaRepository<MyMarginStock, Long> {

    List<MyMarginStock> findAllByUserId(Long userId);
    List<MyMarginStock> findAllByCompanyId(Long companyId);

    MyMarginStock findByTickerAndUserId(String ticker, Long userId);
    MyMarginStock findByTickerAndCompanyId(String ticker, Long companyId);
}
