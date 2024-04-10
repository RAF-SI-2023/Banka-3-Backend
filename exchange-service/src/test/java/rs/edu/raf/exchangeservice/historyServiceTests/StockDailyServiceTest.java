package rs.edu.raf.exchangeservice.historyServiceTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockDaily;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockDailyRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        ticker.setTicker("TEST");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        // Act
        stockDailyService.loadData();

        // Assert
        verify(stockDailyRepository, times(0)).save(any(StockDaily.class));
    }

    @Test
    public void testFindByTicker() {
        // Arrange
        when(stockDailyRepository.findByTicker(anyString())).thenReturn(Collections.emptyList());

        // Act
        stockDailyService.findByTicker("TEST");

        // Assert
        verify(stockDailyRepository, times(1)).findByTicker("TEST");
    }

    @Test
    public void testSaveData() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01\": { \"2. high\": \"100.0\" } }");

        // Act
        stockDailyService.saveData(jsonNode, "TEST");

        // Assert
        verify(stockDailyRepository, times(1)).save(any(StockDaily.class));
    }

    // Add more tests for other methods in StockDailyService
}