package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;

public interface FutureOrderSellRepository extends JpaRepository<FutureOrderSell, Long> {
}
