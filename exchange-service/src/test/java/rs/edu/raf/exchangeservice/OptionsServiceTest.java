package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.listingService.OptionService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionsServiceTest {

    @Mock
    private OptionRepository optionRepository;
    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OptionService optionService;

    @Test
    public void testLoadData() throws Exception {
        // Arrange
        Ticker ticker1 = new Ticker();
        ticker1.setTicker("A");
        Ticker ticker2 = new Ticker();
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker1, ticker2));

        // Act
        optionService.loadData();

        // Assert
        verify(tickerRepository, times(1)).findAll();
        verify(optionRepository, atLeastOnce()).save(any(Option.class));
    }

    @Test
    public void testFindCalls() {
        // Arrange
        Option option1 = new Option();
        Option option2 = new Option();
        when(optionRepository.findByStockListingAndOptionType(anyString(), eq("Calls"))).thenReturn(Arrays.asList(option1, option2));

        // Act
        List<Option> options = optionService.findCalls("AAPL");

        // Assert
        verify(optionRepository, times(1)).findByStockListingAndOptionType(anyString(), eq("Calls"));
        assertEquals(2, options.size());
    }

    @Test
    public void testFindPuts() {
        // Arrange
        Option option1 = new Option();
        Option option2 = new Option();
        when(optionRepository.findByStockListingAndOptionType(anyString(), eq("Puts"))).thenReturn(Arrays.asList(option1, option2));

        // Act
        List<Option> options = optionService.findPuts("AAPL");

        // Assert
        verify(optionRepository, times(1)).findByStockListingAndOptionType(anyString(), eq("Puts"));
        assertEquals(2, options.size());
    }
    @Test
    public void testFindAllRefreshed() throws Exception {
        // Arrange
        Option option1 = new Option();
        Option option2 = new Option();
        when(optionRepository.findAll()).thenReturn(Arrays.asList(option1, option2));

        // Act
        List<Option> optionList = optionService.findAllRefreshed();

        // Assert
        verify(optionRepository, times(1)).deleteAll();
        verify(optionRepository, times(1)).findAll();
        assertEquals(2, optionList.size());
    }
}