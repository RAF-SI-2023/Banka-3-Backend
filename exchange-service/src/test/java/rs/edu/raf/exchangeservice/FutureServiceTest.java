package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.service.listingService.FutureService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FutureServiceTest {

    @Mock
    private FutureRepository futureRepository;

    @InjectMocks
    private FutureService futureService;

    @Test
    public void testLoadData() throws Exception {
        // Arrange
        Future future = new Future();
        when(futureRepository.save(any(Future.class))).thenReturn(future);

        // Act
        futureService.loadData();

        // Assert
        verify(futureRepository, atLeastOnce()).save(any(Future.class));
    }

    @Test
    public void testFindAll() {
        // Arrange
        Future future1 = new Future();
        Future future2 = new Future();
        when(futureRepository.findAll()).thenReturn(Arrays.asList(future1, future2));

        // Act
        List<Future> futures = futureService.findAll();

        // Assert
        verify(futureRepository, times(1)).findAll();
        assertEquals(2, futures.size());
    }
}
