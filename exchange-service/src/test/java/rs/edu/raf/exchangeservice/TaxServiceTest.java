package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;
import rs.edu.raf.exchangeservice.repository.TaxStockRepository;
import rs.edu.raf.exchangeservice.service.TaxService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TaxServiceTest {
    @Mock
    private TaxStockRepository taxRepository;

    @InjectMocks
    private TaxService taxStockService;

    @Test
    public void testFindAll() {
        // Kreirajte dummy podatke za TaxStock
        TaxStock taxStock1 = new TaxStock();
        taxStock1.setTaxStockId(1L);
        taxStock1.setAmount(10.0);
        taxStock1.setDate(System.currentTimeMillis());

        TaxStock taxStock2 = new TaxStock();
        taxStock2.setTaxStockId(2L);
        taxStock2.setAmount(15.0);
        taxStock2.setDate(System.currentTimeMillis());

        List<TaxStock> dummyTaxStocks = Arrays.asList(taxStock1, taxStock2);

        // Simulirajte ponašanje taxRepository.findAll()
        given(taxRepository.findAll()).willReturn(dummyTaxStocks);

        // Pozovite metodu koju testirate
        List<TaxStock> result = taxStockService.findAll();

        // Proverite rezultat
        assertEquals(dummyTaxStocks.size(), result.size());
        assertEquals(dummyTaxStocks.get(1).getAmount(), result.get(1).getAmount());
    }
}