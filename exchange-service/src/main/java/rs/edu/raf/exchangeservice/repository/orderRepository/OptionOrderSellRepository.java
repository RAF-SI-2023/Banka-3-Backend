package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrderSell;

public interface OptionOrderSellRepository extends JpaRepository<OptionOrderSell,Long> {
}
