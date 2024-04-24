package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;

import java.util.List;

@Repository
public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
    List<StockOrder> findByEmployeeId (Long employeeId);
    StockOrder findByStockOrderId (Long id);
}
