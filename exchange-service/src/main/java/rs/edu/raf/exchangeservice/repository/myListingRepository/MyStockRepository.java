package rs.edu.raf.exchangeservice.repository.myListingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;

import java.util.List;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface MyStockRepository extends JpaRepository<MyStock, Long> {
    MyStock findByMyStockId(Long id);
    List<MyStock> findAllByUserId(Long userId);
    List<MyStock> findAllByCompanyId(Long companyId);
    MyStock findByTickerAndUserId(String ticker, Long userId);
    MyStock findByTickerAndCompanyId(String ticker, Long companyId);
    MyStock findByTicker(String ticker);
    List<MyStock> findByUserIdIsNotNullAndCompanyIdIsNullAndPublicAmountGreaterThanAndUserIdNot(Integer publicAmount, Long excludeUserId);
    List<MyStock> findByCompanyIdIsNotNullAndUserIdIsNullAndPublicAmountGreaterThanAndCompanyIdNot(Integer publicAmount, Long excludeCompanyId);
}
