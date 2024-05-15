package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {
    @Mock
    TickerRepository tickerRepository;
    @Mock
    private OptionRepository optionRepository;
    @InjectMocks
    private OptionService optionService;


    @Test
    void loadDataTest() throws JsonProcessingException {
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
        String ticker = "IBM";
        String optionType = "Calls";

        List<Option> options = createDummyCallsOptions(ticker, optionType);

        given(optionRepository.findByStockListingAndOptionType(ticker, optionType)).willReturn(options);

        List<Option> optionList = optionService.findCalls(ticker);

        for(Option option: optionList) {
            boolean found = false;
            for(Option option1: options) {
                if(Objects.equals(option1.getOptionId(), option.getOptionId()) &&
                        option1.getStockListing().equals(option.getStockListing()) &&
                        option1.getOptionType().equals(option.getOptionType())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                fail("Option not found!");
            }
        }
    }

    @Test
    public void testFindPuts() {
        String ticker = "MS";
        String optionType = "Puts";

        List<Option> options = createDummyCallsOptions(ticker, optionType);

        given(optionRepository.findByStockListingAndOptionType(ticker, optionType)).willReturn(options);

        List<Option> optionList = optionService.findPuts(ticker);

        for(Option option: optionList) {
            boolean found = false;
            for(Option option1: options) {
                if(Objects.equals(option1.getOptionId(), option.getOptionId()) &&
                        option1.getStockListing().equals(option.getStockListing()) &&
                        option1.getOptionType().equals(option.getOptionType())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                fail("Option not found!");
            }
        }
    }

    private List<Option> createDummyCallsOptions(String ticker, String optionType) {
        Option option1 = new Option();
        option1.setStockListing(ticker);
        option1.setOptionId(1L);
        option1.setOptionType(optionType);

        Option option2 = new Option();
        option2.setStockListing(ticker);
        option2.setOptionId(2L);
        option2.setOptionType(optionType);

        Option option3 = new Option();
        option3.setStockListing(ticker);
        option3.setOptionId(3L);
        option3.setOptionType(optionType);

        return List.of(option1, option2, option3);
    }
}