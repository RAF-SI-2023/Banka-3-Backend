package rs.edu.raf.exchangeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.dto.StockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.helper.GlobalQuote;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.listingService.StockService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockService stockService;

    @Test
    public void testLoadData() {
        // Arrange
        Ticker ticker = new Ticker();
        ticker.setTicker("A");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));
        // Act
        stockService.loadData();

        // Assert
        verify(tickerRepository, times(1)).findAll();
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    public void testFindAll() {
        // Arrange
        Stock stock1 = new Stock();
        Stock stock2 = new Stock();
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock1, stock2));

        // Act
        List<StockDto> stocks = stockService.findAll();

        // Assert
        verify(stockRepository, times(1)).findAll();
        assertEquals(2, stocks.size());
    }

    @Test
    public void testFindByTicker() {
        // Arrange
        Stock stock = new Stock();
        stock.setTicker("A");
        when(stockRepository.findByTicker("A")).thenReturn(Optional.of(stock));

        // Act
        StockDto result = stockService.findByTicker("A");

        // Assert
        verify(stockRepository, times(1)).findByTicker("A");
        assertEquals("A", result.getTicker());
    }

    @Test
    public void testFindAllRefreshed() throws JsonProcessingException {
        // Arrange
        Stock stock1 = new Stock();
        Stock stock2 = new Stock();
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock1, stock2));

        // Act
        List<Stock> stocks = stockService.findAllRefreshed();

        // Assert
        verify(stockRepository, times(1)).deleteAll();
        verify(stockRepository, times(1)).findAll();
        assertEquals(2, stocks.size());
    }
}