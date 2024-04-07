package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.Exchange;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    Exchange findByExchange(String exchange);
}
