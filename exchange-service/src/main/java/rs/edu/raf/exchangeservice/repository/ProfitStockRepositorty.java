package rs.edu.raf.exchangeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.exchangeservice.domain.model.ProfitStock;

import java.util.List;

public interface ProfitStockRepositorty extends JpaRepository<ProfitStock, Long>{

    List<ProfitStock> findAllByEmployeeId(Long employeeId);
}
