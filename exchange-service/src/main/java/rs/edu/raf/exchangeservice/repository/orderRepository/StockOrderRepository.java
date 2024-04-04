package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;

import java.util.List;

public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
    List<StockOrder> findByEmployeeId (Long employeeId);
    StockOrder findByStockOrderId (Long id);
}
