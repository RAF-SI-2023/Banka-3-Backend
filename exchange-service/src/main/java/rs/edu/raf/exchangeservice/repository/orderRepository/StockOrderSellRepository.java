package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;

@Repository
public interface StockOrderSellRepository extends JpaRepository<StockOrderSell, Long> {
}
