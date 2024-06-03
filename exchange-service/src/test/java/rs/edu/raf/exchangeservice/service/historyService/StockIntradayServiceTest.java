package rs.edu.raf.exchangeservice.service.historyService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockIntradayRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockIntradayServiceTest {
    @Mock
    private StockIntradayRepository stockIntradayRepository;

    @InjectMocks
    private StockIntradayService stockIntradayService;

    @Test
    public void testFindByTicker() {
        // Priprema
        String ticker = "AAPL";
        StockIntraday stock1 = new StockIntraday();
        stock1.setTicker(ticker);
        StockIntraday stock2 = new StockIntraday();
        stock2.setTicker(ticker);
        List<StockIntraday> expectedStocks = Arrays.asList(stock1, stock2);
        when(stockIntradayRepository.findByTicker(ticker)).thenReturn(expectedStocks);

        // Izvršenje
        List<StockIntraday> actualStocks = stockIntradayService.findByTicker(ticker);

        // Provera
        assertEquals(expectedStocks.size(), actualStocks.size());
        for (int i = 0; i < expectedStocks.size(); i++) {
            assertEquals(expectedStocks.get(i).getTicker(), actualStocks.get(i).getTicker());
        }
    }
}