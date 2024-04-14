package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;

import java.util.List;

public interface FutureOrderRepository extends JpaRepository<FutureOrder, Long> {
    List<FutureOrder> findByEmployeeId (Long employeeId);
    FutureOrder findByFutureOrderId (Long id);
}
