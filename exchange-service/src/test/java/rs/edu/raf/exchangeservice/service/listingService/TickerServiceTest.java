package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;
import rs.edu.raf.exchangeservice.service.historyService.StockMonthlyService;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TickerServiceTest {
    @Mock
    private  ExchangeRepository exchangeRepository;
    @Mock
    private  TickerRepository tickerRepository;
    @Mock
    private  StockService stockService;
    @Mock
    private  OptionService optionService;
    @Mock
    private  StockIntradayService stockIntradayService;
    @Mock
    private  StockDailyService stockDailyService;
    @Mock
    private  StockWeeklyService stockWeeklyService;
    @Mock
    private  StockMonthlyService stockMonthlyService;
    @Mock
    private  MyStockService myStockService;
    @Mock
    private ForexService forexService;
    @Mock
    private FutureService futureService;

    @InjectMocks
    private TickerService tickerService;

    @Test
    public void loadDataTest() throws JsonProcessingException {
        Exchange exchange1 = new Exchange();
        exchange1.setExchange("Exchange 1");

        Exchange exchange2 = new Exchange();
        exchange2.setExchange("Exchange 2");

        when(exchangeRepository.findAll()).thenReturn(List.of(exchange1, exchange2));

        tickerService.loadData();

        verify(tickerRepository, atLeastOnce()).save(any(Ticker.class));


    }
}