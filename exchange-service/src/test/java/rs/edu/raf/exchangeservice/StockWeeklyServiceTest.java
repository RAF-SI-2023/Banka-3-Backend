package rs.edu.raf.exchangeservice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockWeekly;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockWeeklyRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        // Act
        stockWeeklyService.loadData();

        // Assert
        /*erify(stockMonthlyRepository, times(61)).save(any(StockMonthly.class));*/
        verify(stockWeeklyRepository, atLeastOnce()).save(any(StockWeekly.class));;
    }

    @Test
    public void testSaveData() throws Exception {
        // Arrange
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01\": { \"2. high\": \"100.0\" }, \"2022-01-02\": { \"2. high\": \"200.0\" } }");
        when(stockWeeklyRepository.save(any(StockWeekly.class))).thenReturn(new StockWeekly());
        String ticker = "A";
        // Act
        stockWeeklyService.saveData(jsonNode, ticker);

        // Assert
        ArgumentCaptor<StockWeekly> captor = ArgumentCaptor.forClass(StockWeekly.class);
        verify(stockWeeklyRepository, times(2)).save(captor.capture());
        List<StockWeekly> capturedStocks = captor.getAllValues();

        assertEquals(2, capturedStocks.size());
        assertEquals(ticker, capturedStocks.get(0).getTicker());
        assertEquals(100.0, capturedStocks.get(0).getPrice());
        assertEquals(ticker, capturedStocks.get(1).getTicker());
        assertEquals(200.0, capturedStocks.get(1).getPrice());}

    @Test
    public void testFindByTicker() {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        StockWeekly stock = new StockWeekly();
        stock.setTicker(ticker.getTicker());
        StockWeekly stock1 = new StockWeekly();
        stock.setTicker(ticker.getTicker());
        // Arrange
        when(stockWeeklyRepository.findByTicker(ticker.getTicker())).thenReturn(List.of(stock, stock1));


        // Act
        List<StockWeekly> list = stockWeeklyService.findByTicker("A");


        assertEquals(2, list.size());
        assertEquals(stock, list.get(0));
        assertEquals(stock1, list.get(1));
    }
}