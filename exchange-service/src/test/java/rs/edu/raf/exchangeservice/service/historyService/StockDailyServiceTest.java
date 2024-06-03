package rs.edu.raf.exchangeservice.service.historyService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockDailyRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockDailyServiceTest {
    @Mock
    private StockDailyRepository stockDailyRepository;

    @InjectMocks
    private StockDailyService stockDailyService;

    @Test
    public void testFindByTicker() {
        // Priprema
        String ticker = "AAPL";
        StockDaily stock1 = new StockDaily();
        stock1.setTicker(ticker);
        StockDaily stock2 = new StockDaily();
        stock2.setTicker(ticker);
        List<StockDaily> expectedStocks = Arrays.asList(stock1, stock2);
        when(stockDailyRepository.findByTicker(ticker)).thenReturn(expectedStocks);

        // Izvršenje
        List<StockDaily> actualStocks = stockDailyService.findByTicker(ticker);

        // Provera
        assertEquals(expectedStocks.size(), actualStocks.size());
        for (int i = 0; i < expectedStocks.size(); i++) {
            assertEquals(expectedStocks.get(i).getTicker(), actualStocks.get(i).getTicker());
        }
    }
}