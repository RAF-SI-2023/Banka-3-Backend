package rs.edu.raf.exchangeservice.repository.orderRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface FutureOrderSellRepository extends JpaRepository<FutureOrderSell, Long> {
}
