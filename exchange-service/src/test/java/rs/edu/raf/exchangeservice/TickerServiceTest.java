package rs.edu.raf.exchangeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.domain.model.helper.Result;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TickerServiceTest {

    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private ExchangeRepository exchangeRepository;
    @InjectMocks
    private TickerService tickerService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadData() throws JsonProcessingException {
        // Arrange
        Exchange exchange1 = new Exchange();
        exchange1.setExchange("NYSE");
        exchange1.setCurrency("USD");
        Exchange exchange2 = new Exchange();
        exchange2.setExchange("NASDAQ");
        exchange2.setCurrency("USD");
        Exchange exchange3 = new Exchange();
        exchange2.setExchange("NASDAQ");
        exchange2.setCurrency("USD");
        Exchange exchange4 = new Exchange();
        exchange2.setExchange("NASDAQ");
        exchange2.setCurrency("USD");
        given(exchangeRepository.findAll()).willReturn(List.of(exchange1, exchange2,exchange3,exchange4));
        // Act
        tickerService.loadData();

        //does not pass the test

        // Assert
        verify(exchangeRepository, times(1)).findAll();
        verify(tickerRepository, times(1)).save(any(Ticker.class));
    }
}