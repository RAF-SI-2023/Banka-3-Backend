package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

@ExtendWith(MockitoExtension.class)
public class TickerServiceTests {

    @Mock
    private TickerRepository tickerRepository;

    @InjectMocks
    private TickerService tickerService;

}
