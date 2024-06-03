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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void testLoadData() throws IOException {
        // Arrange
        String testCsvData = "Exchange Name,Exchange Acronym,Exchange,Country,Currency,Time Zone,Open Time,Close Time\n"
                + "New York Stock Exchange,NYSE,US,USA,US Dollar,America/New_York,09:30,16:00\n"
                + "London Stock Exchange,LSE,UK,United Kingdom,British Pound Sterling,Europe/London,08:00,16:30";

        InputStream inputStream = new ByteArrayInputStream(testCsvData.getBytes());

        when(exchangeRepository.save(any(Exchange.class))).thenReturn(new Exchange()); // Simuliramo pohranu u repozitorij

        // Act
        exchangeService.loadData();

        // Assert
        verify(exchangeRepository, times(93)).save(any(Exchange.class)); // Očekujemo da će se metoda save pozvati dvaput
    }
}

