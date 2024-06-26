package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;
import rs.edu.raf.exchangeservice.repository.TaxStockRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxService {

    private final TaxStockRepository taxRepository;

    public List<TaxStock> findAll() {
        List<TaxStock> taxStocks = taxRepository.findAll();

        return taxStocks.stream()
                .sorted((tax1, tax2) -> tax2.getDate().compareTo(tax1.getDate())) // Sortiranje po datumu, najnoviji prvo
                .collect(Collectors.toList());
    }
}
