package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;

import java.util.List;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface FutureOrderRepository extends JpaRepository<FutureOrder, Long> {

    List<FutureOrder> findByEmployeeId (Long employeeId);

    FutureOrder findByFutureOrderId (Long id);
}
