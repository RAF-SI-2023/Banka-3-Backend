package rs.edu.raf.exchangeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockIntradayRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));

        // Act
        stockIntradayService.loadData();

        // Assert
        /*erify(stockMonthlyRepository, times(61)).save(any(StockMonthly.class));*/
        verify(stockIntradayRepository, atLeastOnce()).save(any(StockIntraday.class));
    }

    @Test
    public void testSaveData() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{ \"2022-01-01 00:00:00\": { \"2. high\": \"100.0\" }, \"2022-01-02 00:00:00\": { \"2. high\": \"200.0\" } }");
        when(stockIntradayRepository.save(any(StockIntraday.class))).thenReturn(new StockIntraday());
        String ticker = "A";
        // Act
        stockIntradayService.saveData(jsonNode, ticker);

        // Assert
        ArgumentCaptor<StockIntraday> captor = ArgumentCaptor.forClass(StockIntraday.class);
        verify(stockIntradayRepository, times(2)).save(captor.capture());
        List<StockIntraday> capturedStocks = captor.getAllValues();

        assertEquals(2, capturedStocks.size());
        assertEquals(ticker, capturedStocks.get(0).getTicker());
        assertEquals(100.0, capturedStocks.get(0).getPrice());
        assertEquals(ticker, capturedStocks.get(1).getTicker());
        assertEquals(200.0, capturedStocks.get(1).getPrice());
    }

    @Test
    public void testFindByTicker() {
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        StockIntraday stock = new StockIntraday();
        stock.setTicker(ticker.getTicker());
        StockIntraday stock1 = new StockIntraday();
        stock.setTicker(ticker.getTicker());
        // Arrange
        when(stockIntradayRepository.findByTicker(ticker.getTicker())).thenReturn(List.of(stock, stock1));


        // Act
        List<StockIntraday> list = stockIntradayService.findByTicker("A");


        assertEquals(2, list.size());
        assertEquals(stock, list.get(0));
        assertEquals(stock1, list.get(1));
    }
}