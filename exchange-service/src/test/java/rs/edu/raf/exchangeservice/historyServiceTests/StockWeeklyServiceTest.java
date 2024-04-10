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
import rs.edu.raf.exchangeservice.domain.model.history.StockWeekly;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockWeeklyRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockWeeklyServiceTest {

    @Mock
    private StockWeeklyRepository stockWeeklyRepository;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockWeeklyService stockWeeklyService;

    @Test
    public void testLoadData() throws Exception {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("TEST");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        stockWeeklyService.loadData();

        // Assert
        verify(stockWeeklyRepository, times(0)).save(any(StockWeekly.class));
    }

    @Test
    public void testSaveData() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01\": { \"2. high\": \"100.0\" } }");

        // Act
        stockWeeklyService.saveData(jsonNode, "TEST");

        // Assert
        verify(stockWeeklyRepository, times(1)).save(any(StockWeekly.class));
    }

    @Test
    public void testFindByTicker() {
        // Arrange
        when(stockWeeklyRepository.findByTicker(anyString())).thenReturn(Collections.emptyList());

        // Act
        stockWeeklyService.findByTicker("TEST");

        // Assert
        verify(stockWeeklyRepository, times(1)).findByTicker("TEST");
    }
}