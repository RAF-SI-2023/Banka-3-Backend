package rs.edu.raf.exchangeservice.service.listingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.listing.StockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    @Mock
    private StockRepository stockRepository;
    @Mock
    private TickerRepository tickerRepository;

    @InjectMocks
    private StockService stockService;


    @Test
    void loadDataTest() {
        Ticker ticker = new Ticker();
        ticker.setTicker("AAPL");
        ticker.setName("Apple Inc.");
        ticker.setPrimaryExchange("Exchange 1");
        ticker.setCurrencyName("USD");

        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker));
        // Act
        stockService.loadData();

        // Assert
        verify(tickerRepository, times(1)).findAll();
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    public void testFindAll() {
        List<Stock> stockList = createDummyStocks();

        given(stockRepository.findAll()).willReturn(stockList);

        List<StockDto> stockDtoList = stockService.findAll();
        for(StockDto stockDto: stockDtoList) {
            boolean found = false;
            for(Stock stock: stockList) {
                if(Objects.equals(stockDto.getStockId(), stock.getStockId())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                fail("Stock not found!");
            }
        }
    }

    @Test
    public void testFindByTicker() {
        String ticker = "ticker";
        Stock stock = createDummyStock();
        stock.setTicker(ticker);

        given(stockRepository.findByTicker(ticker)).willReturn(Optional.of(stock));

        StockDto stockDto = stockService.findByTicker(ticker);
        assertEquals(stock.getTicker(), stockDto.getTicker());
    }

    private List<Stock> createDummyStocks() {
        Stock stock1 = new Stock();
        stock1.setStockId(1L);
        stock1.setName("Apple");
        stock1.setExchange("NYSE");
        stock1.setTicker("APPL");
        stock1.setPrice(100.00);
        stock1.setCurrencyMark("USD");

        Stock stock2 = new Stock();
        stock2.setStockId(2L);
        stock2.setName("Microsoft");
        stock2.setExchange("NYSE");
        stock2.setTicker("MS");
        stock2.setPrice(100.00);
        stock2.setCurrencyMark("USD");

        Stock stock3 = new Stock();
        stock3.setStockId(3L);
        stock3.setName("IBM");
        stock3.setExchange("NYSE");
        stock3.setTicker("IBM");
        stock3.setPrice(100.00);
        stock3.setCurrencyMark("USD");

        return List.of(stock1, stock2, stock3);
    }

    private Stock createDummyStock() {
        Stock stock = new Stock();
        stock.setStockId(1L);
        stock.setName("Apple");
        stock.setExchange("NYSE");
        stock.setTicker("APPL");
        stock.setPrice(100.00);
        stock.setCurrencyMark("USD");

        return stock;
    }
}