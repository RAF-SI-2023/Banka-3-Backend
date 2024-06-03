package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.repository.listingRepository.ForexRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForexServiceTest {
    @InjectMocks
    private ForexService forexService;

    @Mock
    private ForexRepository forexRepository;

    @Test
    void testFindAll() {
        // Priprema testnih podataka
        Forex forex1 = new Forex();
        Forex forex2 = new Forex();
        List<Forex> expectedForexList = Arrays.asList(forex1, forex2);
        when(forexRepository.findAll()).thenReturn(expectedForexList);

        // Poziv metode findAll
        List<Forex> actualForexList = forexService.findAll();

        // Provera da li su povratna lista i očekivana lista jednake
        assertEquals(expectedForexList, actualForexList);
    }
    @Test
    void testFindAllRefreshed() throws JsonProcessingException {
        // Priprema testnih podataka
        List<Forex> expectedForexList = Arrays.asList(new Forex(), new Forex());
        when(forexRepository.findAll()).thenReturn(expectedForexList);

        // Poziv metode findAllRefreshed
        List<Forex> actualForexList = forexService.findAllRefreshed();

        // Provera da li je deleteAll pozvana na repozitorijumu
        verify(forexRepository, times(1)).deleteAll();


        // Provera da li su povratna lista i očekivana lista jednake
        assertEquals(expectedForexList, actualForexList);
    }
}