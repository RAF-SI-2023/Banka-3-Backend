package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.service.ExchangeService;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
public class ExchangeServiceTest {
    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private TickerService tickerService;

    @InjectMocks
    private ExchangeService exchangeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findAll() {
        Exchange exchange1 = new Exchange();
        Exchange exchange2 = new Exchange();
        when(exchangeRepository.findAll()).thenReturn(Arrays.asList(exchange1, exchange2));

        List<Exchange> result = exchangeService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void findByExchangeMark_ReturnsCorrectExchange() {
        Exchange exchange = new Exchange();
        exchange.setExchange("NASDAQ");
        when(exchangeRepository.findByExchange("NASDAQ")).thenReturn(exchange);

        Exchange result = exchangeService.findByExchangeMark("NASDAQ");

        assertEquals("NASDAQ", result.getExchange());
    }

    @Test
    public void findByExchangeMark_ReturnsNullWhenExchangeNotFound() {
        when(exchangeRepository.findByExchange("NASDAQ")).thenReturn(null);

        Exchange result = exchangeService.findByExchangeMark("NASDAQ");

        assertEquals(null, result);
    }
}

