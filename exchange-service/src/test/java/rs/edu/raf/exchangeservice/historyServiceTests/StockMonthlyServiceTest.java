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
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockMonthlyRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockMonthlyService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockMonthlyServiceTest {

    @Mock
    private StockMonthlyRepository stockMonthlyRepository;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockMonthlyService stockMonthlyService;

    @Test
    public void testLoadDataMonthly() throws Exception {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        // Act
        stockMonthlyService.loadData();

        // Assert
        /*erify(stockMonthlyRepository, times(61)).save(any(StockMonthly.class));*/
        verify(stockMonthlyRepository, atLeastOnce()).save(any(StockMonthly.class));
    }

    @Test
    public void testSaveDataMonthly() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01\": { \"2. high\": \"100.0\" } }");
        when(stockMonthlyRepository.save(any(StockMonthly.class))).thenReturn(new StockMonthly());

        // Act
        stockMonthlyService.saveData(jsonNode, "TEST");

        // Assert
        verify(stockMonthlyRepository, times(1)).save(any(StockMonthly.class));
    }

    @Test
    public void testFindByTickerMonthly() {
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        StockMonthly stock = new StockMonthly();
        stock.setTicker(ticker.getTicker());
        StockMonthly stock1 = new StockMonthly();
        stock.setTicker(ticker.getTicker());
        // Arrange
        when(stockMonthlyRepository.findByTicker(ticker.getTicker())).thenReturn(List.of(stock, stock1));


        // Act
        List<StockMonthly> list = stockMonthlyService.findByTicker("A");


        assertEquals(2, list.size());
        assertEquals(stock, list.get(0));
        assertEquals(stock1, list.get(1));
    }
}