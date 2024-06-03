package rs.edu.raf.exchangeservice.service.historyService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockMonthlyRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockMonthlyServiceTest {
    @Mock
    private StockMonthlyRepository stockMonthlyRepository;

    @InjectMocks
    private StockMonthlyService stockMonthlyService;

    @Test
    public void testFindByTicker() {
        // Priprema
        String ticker = "AAPL";
        StockMonthly stock1 = new StockMonthly();
        stock1.setTicker(ticker);
        StockMonthly stock2 = new StockMonthly();
        stock2.setTicker(ticker);
        List<StockMonthly> expectedStocks = Arrays.asList(stock1, stock2);
        when(stockMonthlyRepository.findByTicker(ticker)).thenReturn(expectedStocks);

        // Izvršenje
        List<StockMonthly> actualStocks = stockMonthlyService.findByTicker(ticker);

        // Provera
        assertEquals(expectedStocks.size(), actualStocks.size());
        for (int i = 0; i < expectedStocks.size(); i++) {
            assertEquals(expectedStocks.get(i).getTicker(), actualStocks.get(i).getTicker());
        }
    }
}