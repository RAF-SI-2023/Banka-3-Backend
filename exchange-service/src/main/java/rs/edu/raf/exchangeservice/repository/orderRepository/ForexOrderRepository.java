package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.order.ForexOrder;

import java.util.List;

public interface ForexOrderRepository extends JpaRepository<ForexOrder, Long>{

    List<ForexOrder> findAllByCompanyId(Long companyId);
}
