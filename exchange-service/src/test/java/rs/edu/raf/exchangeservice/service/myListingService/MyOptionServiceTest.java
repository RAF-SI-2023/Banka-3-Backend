package rs.edu.raf.exchangeservice.service.myListingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.option.OptionUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyOptionServiceTest {
    @InjectMocks
    private MyOptionService myOptionService;

    @Mock
    private MyOptionRepository myOptionRepository;

    @Mock
    private BankServiceClient bankServiceClient;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void findByContractSymbol_ExistingOption_ReturnsOption() {
        // Priprema
        String contractSymbol = "AAPL220701C00500000";
        MyOption expectedOption = new MyOption();
        expectedOption.setContractSymbol(contractSymbol);
        when(myOptionRepository.findByContractSymbol(contractSymbol)).thenReturn(expectedOption);

        // Poziv metode koju testiramo
        MyOption result = myOptionService.findByContractSymbol(contractSymbol);

        // Provera
        assertNotNull(result);
        assertEquals(expectedOption, result);
        verify(myOptionRepository, times(1)).findByContractSymbol(contractSymbol);
    }

    @Test
    void findByContractSymbol_NonExistingOption_ReturnsNull() {
        // Priprema
        String contractSymbol = "NonExistingOption";
        when(myOptionRepository.findByContractSymbol(contractSymbol)).thenReturn(null);

        // Poziv metode koju testiramo
        MyOption result = myOptionService.findByContractSymbol(contractSymbol);

        // Provera
        assertNull(result);
        verify(myOptionRepository, times(1)).findByContractSymbol(contractSymbol);
    }

    @Test
    void findAllByCompanyId_ExistingOptions_ReturnsOptions() {
        // Priprema
        Long companyId = 1L;
        List<MyOption> expectedOptions = createDummyOptions(companyId);
        when(myOptionRepository.findAllByCompanyId(companyId)).thenReturn(expectedOptions);

        // Poziv metode koju testiramo
        List<MyOption> result = myOptionService.findAllByCompanyId(companyId);

        // Provera
        assertNotNull(result);
        assertEquals(expectedOptions.size(), result.size());
        assertEquals(expectedOptions, result);
        verify(myOptionRepository, times(1)).findAllByCompanyId(companyId);
    }

    @Test
    void findAllByCompanyId_NoOptions_ReturnsEmptyList() {
        // Priprema
        Long companyId = 1L;
        when(myOptionRepository.findAllByCompanyId(companyId)).thenReturn(new ArrayList<>());

        // Poziv metode koju testiramo
        List<MyOption> result = myOptionService.findAllByCompanyId(companyId);

        // Provera
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(myOptionRepository, times(1)).findAllByCompanyId(companyId);
    }

    // Pomoćna metoda za kreiranje dummy opcija
    private List<MyOption> createDummyOptions(Long companyId) {
        List<MyOption> options = new ArrayList<>();
        options.add(createDummyOption(companyId, "AAPL220701C00500000"));
        options.add(createDummyOption(companyId, "GOOG220701C01000000"));
        // Dodajte koliko god dummy opcija želite
        return options;
    }

    // Pomoćna metoda za kreiranje dummy opcije
    private MyOption createDummyOption(Long companyId, String contractSymbol) {
        MyOption option = new MyOption();
        option.setCompanyId(companyId);
        option.setContractSymbol(contractSymbol);
        // Postavite ostale podatke po potrebi
        return option;
    }
    @Test
    void sellOptionsToExchange_SuccessfulSale() {
        // Postavljamo mock ponašanje za myOptionRepository
        String contractSymbol = "AAPL220701C00500000";
        MyOption myOption = new MyOption(null, null, contractSymbol, null, 0.0, 0.0, 100, "USD", 5);
        when(myOptionRepository.findByContractSymbol(contractSymbol)).thenReturn(myOption);

        SellOptionDto sellOptionDto = new SellOptionDto();
        sellOptionDto.setContractSymbol(contractSymbol);
        sellOptionDto.setCompanyId(1L);
        sellOptionDto.setQuantity(0);

        Option option = new Option();
        option.setContractSymbol(contractSymbol);
        option.setOpenInterest(10); // Some existing open interest
        option.setAsk(100);

        BankTransactionDto bankTransactionDto = new BankTransactionDto();
        bankTransactionDto.setCompanyId(sellOptionDto.getCompanyId());
        bankTransactionDto.setUserId(null);
        bankTransactionDto.setAmount(sellOptionDto.getQuantity() * option.getAsk());
        bankTransactionDto.setCurrencyMark(myOption.getCurrencyMark());

        // Postavljamo mock ponašanje za optionRepository
        when(optionRepository.findByContractSymbol(contractSymbol)).thenReturn(option);
        when(bankServiceClient.stockSellTransaction(bankTransactionDto)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        // Postavljamo mock ponašanje za bankServiceClient
        //doNothing().when(bankServiceClient).stockSellTransaction(any());

        // Pozivamo metodu koju testiramo
        myOptionService.sellOptionsToExchange(sellOptionDto);

        // Proveravamo da li su odgovarajuće metode pozvane
        verify(myOptionRepository, times(1)).findByContractSymbol(contractSymbol);
        verify(optionRepository, times(1)).findByContractSymbol(contractSymbol);
        verify(bankServiceClient, times(1)).stockSellTransaction(any());
        verify(myOptionRepository, times(1)).save(myOption);
        verify(optionRepository, times(1)).save(option);
        verify(eventPublisher, times(1)).publishEvent(any(OptionUpdateEvent.class));

        // Provera da li su se podaci ispravno ažurirali
        assertEquals(5, myOption.getQuantity()); // Novo stanje količine
        assertEquals(10, option.getOpenInterest()); // Open Interest se povećao za 10
    }
    @Test
    void sellOptionsToExchange_NotEnoughOptions() {
        // Prepare
        String contractSymbol = "AAPL220701C00500000";
        int quantity = 10; // More than available quantity
        SellOptionDto sellOptionDto = new SellOptionDto();
        sellOptionDto.setContractSymbol(contractSymbol);
        sellOptionDto.setQuantity(quantity);

        MyOption myOption = new MyOption();
        myOption.setContractSymbol(contractSymbol);
        myOption.setQuantity(5); // Available quantity

        when(myOptionRepository.findByContractSymbol(contractSymbol)).thenReturn(myOption);

        // Call the method
        assertThrows(RuntimeException.class, () -> myOptionService.sellOptionsToExchange(sellOptionDto));

        // Verify that methods were not called
        verify(myOptionRepository, times(1)).findByContractSymbol(contractSymbol);
    }
    @Test
    void sellOptionsToExchange_OptionNotFound() {
        // Prepare
        String contractSymbol = "AAPL220701C00500000";
        SellOptionDto sellOptionDto = new SellOptionDto();
        sellOptionDto.setContractSymbol(contractSymbol);

        when(myOptionRepository.findByContractSymbol(contractSymbol)).thenReturn(null);

        // Call the method
        assertThrows(RuntimeException.class, () -> myOptionService.sellOptionsToExchange(sellOptionDto));

        // Verify that methods were not called
        verify(myOptionRepository, times(1)).findByContractSymbol(contractSymbol);
    }
}