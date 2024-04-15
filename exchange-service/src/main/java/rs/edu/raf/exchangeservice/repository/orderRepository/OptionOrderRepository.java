package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrder;

import java.util.List;

public interface OptionOrderRepository extends JpaRepository<OptionOrder, Long> {

    List<OptionOrder> findByEmployeeId (Long employeeId);
    OptionOrder findByOptionOrderId(Long id);
}