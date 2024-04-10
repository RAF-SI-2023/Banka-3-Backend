package rs.edu.raf.exchangeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockDailyRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockDailyServiceTest {

    @Mock
    private StockDailyRepository stockDailyRepository;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockDailyService stockDailyService;

    @Test
    public void testLoadData() throws Exception {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        // Act
        stockDailyService.loadData();

        // Assert
        /*erify(stockMonthlyRepository, times(61)).save(any(StockMonthly.class));*/
        verify(stockDailyRepository, atLeastOnce()).save(any(StockDaily.class));
    }

    @Test
    public void testFindByTicker() {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        StockDaily stock = new StockDaily();
        stock.setTicker(ticker.getTicker());
        StockDaily stock1 = new StockDaily();
        stock.setTicker(ticker.getTicker());
        // Arrange
        when(stockDailyRepository.findByTicker(ticker.getTicker())).thenReturn(List.of(stock, stock1));


        // Act
        List<StockDaily> list = stockDailyRepository.findByTicker("A");


        assertEquals(2, list.size());
        assertEquals(stock, list.get(0));
        assertEquals(stock1, list.get(1));
    }

    @Test
    public void testSaveData() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01\": { \"2. high\": \"100.0\" } }");
        when(stockDailyRepository.save(any(StockDaily.class))).thenReturn(new StockDaily());

        // Act
        stockDailyService.saveData(jsonNode, "A");

        // Assert
        verify(stockDailyRepository, times(1)).save(any(StockDaily.class));
    }

    // Add more tests for other methods in StockDailyService
}