package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;

public interface StockOrderSellRepository extends JpaRepository<StockOrderSell, Long> {
}
