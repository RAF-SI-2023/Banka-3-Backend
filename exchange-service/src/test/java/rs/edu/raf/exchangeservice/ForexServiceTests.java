package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.repository.listingRepository.ForexRepository;
import rs.edu.raf.exchangeservice.service.listingService.ForexService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ForexServiceTests {

    @Mock
    private ForexRepository forexRepository;

    @InjectMocks
    private ForexService forexService;

    @Test
    public void testFindAll() {

        List<Forex> forexes = createDummyForexes();

        given(forexRepository.findAll()).willReturn(forexes);

        List<Forex> forexList = forexService.findAll();
        for(Forex forex: forexList) {
            boolean found = false;
            for(Forex forex1: forexes) {
                if(Objects.equals(forex.getForexId(), forex1.getForexId())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                fail("Forex not found!");
            }
        }
    }

    private List<Forex> createDummyForexes() {
        Forex forex1 = new Forex();
        forex1.setForexId(1L);
        forex1.setBaseCurrency("USD");
        forex1.setQuoteCurrency("EUR");
        forex1.setConversionRate(0.85);

        Forex forex2 = new Forex();
        forex2.setForexId(2L);
        forex2.setBaseCurrency("RSD");
        forex2.setQuoteCurrency("EUR");
        forex2.setConversionRate(118.0);

        Forex forex3 = new Forex();
        forex3.setForexId(3L);
        forex3.setBaseCurrency("RSD");
        forex3.setQuoteCurrency("EUR");
        forex3.setConversionRate(110.5);

        return List.of(forex1, forex2, forex3);
    }
}
