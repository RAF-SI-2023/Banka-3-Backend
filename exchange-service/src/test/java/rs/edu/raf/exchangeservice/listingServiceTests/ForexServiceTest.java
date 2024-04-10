package rs.edu.raf.exchangeservice.listingServiceTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.repository.listingRepository.ForexRepository;
import rs.edu.raf.exchangeservice.service.listingService.ForexService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForexServiceTest {

    @Mock
    private ForexRepository forexRepository;

    @InjectMocks
    private ForexService forexService;

    @Test
    public void testLoadData() throws Exception {


        forexService.loadData();

        // Assert
        verify(forexRepository, times(162)).save(any(Forex.class));
    }

    @Test
    public void testFindAll() {
        Forex forex = new Forex();
        Forex forex2 = new Forex();
        when(forexRepository.findAll()).thenReturn(Arrays.asList(forex, forex2));
        // Arrange

        // Act
        List<Forex> forexList = forexService.findAll();

        // Assert
        assertEquals(2, forexList.size());
    }

    @Test
    public void testFindAllRefreshed() throws Exception {
        // Arrange
        when(forexRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        forexService.findAllRefreshed();

        // Assert
        verify(forexRepository, times(1)).deleteAll();
        verify(forexRepository, times(1)).findAll();
    }
}