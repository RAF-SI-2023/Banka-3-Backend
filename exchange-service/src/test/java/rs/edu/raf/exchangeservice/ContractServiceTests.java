package rs.edu.raf.exchangeservice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.service.ContractService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTests {

    @Mock
    ContractRepository contractRepository;
    @Mock
    TickerRepository tickerRepository;
    @Mock
    MyStockRepository myStockRepository;

    @InjectMocks
    ContractService contractService;

    @Test
    void companyAccept_ShouldSetSellerCertificateToAcceptedAndSaveContract() {
        Contract contract = new Contract();
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        boolean result = contractService.companyAccept(new ContractAnswerDto());

        assertTrue(result);
        assertEquals(SellerCertificate.ACCEPTED, contract.getSellerCertificate());
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void companyAccept_ShouldThrowExceptionWhenContractNotFound() {
        when(contractRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contractService.companyAccept(new ContractAnswerDto()));
    }

    @Test
    void companyDecline_ShouldSetSellerCertificateToDeclinedAndSaveContract() {
        Contract contract = new Contract();
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        boolean result = contractService.companyDecline(new ContractAnswerDto());

        assertTrue(result);
        assertEquals(SellerCertificate.DECLINED, contract.getSellerCertificate());
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void companyDecline_ShouldThrowExceptionWhenContractNotFound() {
        when(contractRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contractService.companyDecline(new ContractAnswerDto()));
    }



    @Test
    void supervisorAccept_WhenContractIsProcessing_ShouldReturnTrueAndSaveContract() {
        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.PROCESSING);
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        Ticker ticker = new Ticker();
        ticker.setTicker("AAPL");
        ticker.setCurrencyName("USD");
        when(tickerRepository.findByTicker(any())).thenReturn(ticker);

        // Act
        boolean result = contractService.supervisorAccept(new ContractAnswerDto());

        // Assert
        assertTrue(result);
        verify(contractRepository, times(1)).save(any(Contract.class));
        verify(tickerRepository, times(1)).findByTicker(any());
    }


    @Test
    void supervisorDecline_WhenContractIsProcessing_ShouldReturnTrueAndSaveContract() {
        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.PROCESSING);
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        boolean result = contractService.supervisorDecline(new ContractAnswerDto());

        assertTrue(result);
        verify(contractRepository, times(1)).save(any(Contract.class));
    }


}









