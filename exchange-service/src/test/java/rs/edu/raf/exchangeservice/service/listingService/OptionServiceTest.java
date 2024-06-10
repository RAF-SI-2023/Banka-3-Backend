package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.backend.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.CompanyAccountDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyOptionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {
    @Mock
    TickerRepository tickerRepository;
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private MyOptionRepository myOptionRepository;

    @InjectMocks
    private OptionService optionService;
    @Mock
    BankServiceClient bankServiceClient;



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

    @Test
    public void testFindByContractSymbol_WhenOptionExists() {
        // Mocking an existing option
        Option expectedOption = new Option(/* Provide necessary data for Option */);
        when(optionRepository.findByContractSymbol(anyString())).thenReturn(expectedOption);

        // Call the method
        Option result = optionService.findByContractSymbol("yourContractSymbol");

        // Assert that the result matches the expected option
        assertEquals(expectedOption, result);
    }

    @Test
    public void testFindByContractSymbol_WhenOptionDoesNotExist() {
        // Mocking when option is not found
        when(optionRepository.findByContractSymbol(anyString())).thenReturn(null);

        // Call the method
        Option result = optionService.findByContractSymbol("nonExistentSymbol");

        // Assert that the result is null
        assertEquals(null, result);
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

    @Test
    void testFindAllRefreshed() throws JsonProcessingException {
        // Priprema testnih podataka
        List<Option> expectedOptionsList = Arrays.asList(new Option(), new Option());
        when(optionRepository.findAll()).thenReturn(expectedOptionsList);

        // Poziv metode findAllRefreshed
        List<Option> actualOptionsList = optionService.findAllRefreshed();

        // Provera da li je deleteAll pozvana na repozitorijumu
        verify(optionRepository, times(1)).deleteAll();


        // Provera da li su povratna lista i očekivana lista jednake
        assertEquals(expectedOptionsList, actualOptionsList);
    }

    @Test
    public void testBuyOptionsFromExchange_OptionNotFound() {
        // Mocking Option not found scenario
        when(optionRepository.findByContractSymbol(any())).thenReturn(null);

        BuyOptionDto buyOptionDto = new BuyOptionDto();

        // Call the method, should throw RuntimeException
        try {
            optionService.buyOptionsFromExchange(buyOptionDto);
            // If the method doesn't throw an exception, fail the test
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            // Expected RuntimeException was thrown
            // You can add more assertions here if needed
        }
    }

    @Test
    public void testBuyOptionsFromExchange_NotEnoughOptionsAvailable() {
        // Mocking data
        Option mockOption = new Option(/* Provide necessary data */);
        when(optionRepository.findByContractSymbol(any())).thenReturn(mockOption);

        BuyOptionDto buyOptionDto = new BuyOptionDto(/* Provide necessary data */);

        // Making quantity higher than open interest to simulate not enough options available
        when(bankServiceClient.stockBuyTransaction(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Call the method, should throw RuntimeException
        try {
            optionService.buyOptionsFromExchange(buyOptionDto);
            // If the method doesn't throw an exception, fail the test
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            // Expected RuntimeException was thrown
            // You can add more assertions here if needed
        }
    }
}