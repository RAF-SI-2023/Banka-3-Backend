package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;
import rs.edu.raf.exchangeservice.repository.TaxStockRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxStockRepository taxRepository;

    public List<TaxStock> findAll() {
        return taxRepository.findAll();
    }
}
