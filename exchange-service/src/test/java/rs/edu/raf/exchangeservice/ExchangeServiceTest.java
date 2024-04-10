package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.ExchangeService;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private ExchangeRepository exchangeRepository;
    @Mock
    private TickerService tickerService;
    @InjectMocks
    private ExchangeService exchangeService;


    @Test
    public void testFindAll() {
        // Arrange
        Exchange exchange1 = new Exchange();
        Exchange exchange2 = new Exchange();
        when(exchangeRepository.findAll()).thenReturn(List.of(exchange1, exchange2));

        // Act
        List<Exchange> result = exchangeService.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByExchange() {
        // Arrange
        String exchangeName = "TestExchange";
        Exchange exchange = new Exchange();
        exchange.setExchangeName(exchangeName);
        when(exchangeRepository.findByExchange(exchangeName)).thenReturn(exchange);

        // Act
        Exchange result = exchangeService.findByExchange(exchangeName);

        // Assert
        assertEquals(exchangeName, result.getExchangeName());
    }

    @Test
public void testLoadData() throws IOException {

    exchangeService.loadData();


    // Assert
    verify(exchangeRepository, times(92)).save(any(Exchange.class));
    verify(tickerService, times(1)).loadData();
}

}