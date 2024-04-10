package rs.edu.raf.exchangeservice.historyServiceTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockIntradayRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockIntradayServiceTest {

    @Mock
    private StockIntradayRepository stockIntradayRepository;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockIntradayService stockIntradayService;

    @Test
    public void testLoadData() throws Exception {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("TEST");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        // Act
        stockIntradayService.loadData();

        // Assert
        verify(stockIntradayRepository, times(0)).save(any(StockIntraday.class));
    }

    @Test
    public void testSaveData() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01 01:00:00\": { \"2. high\": \"100.0\" } }");

        // Act
        stockIntradayService.saveData(jsonNode, "TEST");

        // Assert
        verify(stockIntradayRepository, times(1)).save(any(StockIntraday.class));
    }

    @Test
    public void testFindByTicker() {
        // Arrange
        when(stockIntradayRepository.findByTicker(anyString())).thenReturn(Collections.emptyList());

        // Act
        stockIntradayService.findByTicker("TEST");

        // Assert
        verify(stockIntradayRepository, times(1)).findByTicker("TEST");
    }
}