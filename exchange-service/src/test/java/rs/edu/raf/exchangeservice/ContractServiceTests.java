package rs.edu.raf.exchangeservice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.UserOtcDto;
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
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTests {

    @Mock
    ContractRepository contractRepository;
    @Mock
    private MyStockService myStockService;
    @Mock
    private BankServiceClient bankServiceClient;

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
    void supervisorDecline_WhenContractIsProcessing_ShouldReturnTrueAndSaveContract() {
        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.PROCESSING);
        when(contractRepository.findById(any())).thenReturn(Optional.of(contract));

        boolean result = contractService.supervisorDecline(new ContractAnswerDto());

        assertTrue(result);
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    public void testGetAllUnresolvedContracts() {
        // Arrange
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> expectedContracts = Arrays.asList(contract1, contract2);

        when(contractRepository.findBySellerCertificateAndBankCertificate(SellerCertificate.ACCEPTED, BankCertificate.PROCESSING))
                .thenReturn(expectedContracts);

        // Act
        List<Contract> actualContracts = contractService.getAllUnresolvedContracts();

        // Assert
        assertNotNull(actualContracts);
        assertEquals(2, actualContracts.size());
        assertTrue(actualContracts.contains(contract1));
        assertTrue(actualContracts.contains(contract2));

        verify(contractRepository, times(1)).findBySellerCertificateAndBankCertificate(SellerCertificate.ACCEPTED, BankCertificate.PROCESSING);
    }

    @Test
    public void testGetAllSentContractsByCompanyId() {
        // Arrange
        Long companyId = 1L;
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> expectedContracts = Arrays.asList(contract1, contract2);

        when(contractRepository.findByCompanyBuyerId(companyId)).thenReturn(expectedContracts);

        // Act
        List<Contract> actualContracts = contractService.getAllSentContractsByCompanyId(companyId);

        // Assert
        assertNotNull(actualContracts);
        assertEquals(2, actualContracts.size());
        assertTrue(actualContracts.contains(contract1));
        assertTrue(actualContracts.contains(contract2));

        verify(contractRepository, times(1)).findByCompanyBuyerId(companyId);
    }

    @Test
    public void testGetAllReceivedContractsByCompanyId() {
        // Arrange
        Long companyId = 1L;
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> expectedContracts = Arrays.asList(contract1, contract2);

        when(contractRepository.findByCompanySellerId(companyId)).thenReturn(expectedContracts);

        // Act
        List<Contract> actualContracts = contractService.getAllReceivedContractsByCompanyId(companyId);

        // Assert
        assertNotNull(actualContracts);
        assertEquals(2, actualContracts.size());
        assertTrue(actualContracts.contains(contract1));
        assertTrue(actualContracts.contains(contract2));

        verify(contractRepository, times(1)).findByCompanySellerId(companyId);
    }


    @Test
    public void testGetAllSentContractsByUserId() {
        // Arrange
        Long userId = 1L;
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> expectedContracts = Arrays.asList(contract1, contract2);

        when(contractRepository.findByUserBuyerId(userId)).thenReturn(expectedContracts);

        // Act
        List<Contract> actualContracts = contractService.getAllSentContractsByUserId(userId);

        // Assert
        assertNotNull(actualContracts);
        assertEquals(2, actualContracts.size());
        assertTrue(actualContracts.contains(contract1));
        assertTrue(actualContracts.contains(contract2));

        verify(contractRepository, times(1)).findByUserBuyerId(userId);
    }

    @Test
    public void testGetAllReceivedContractsByUserId() {
        // Arrange
        Long userId = 1L;
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> expectedContracts = Arrays.asList(contract1, contract2);

        when(contractRepository.findByUserSellerId(userId)).thenReturn(expectedContracts);

        // Act
        List<Contract> actualContracts = contractService.getAllReceivedContractsByUserId(userId);

        // Assert
        assertNotNull(actualContracts);
        assertEquals(2, actualContracts.size());
        assertTrue(actualContracts.contains(contract1));
        assertTrue(actualContracts.contains(contract2));

        verify(contractRepository, times(1)).findByUserSellerId(userId);
    }

    @Test
    public void testGetAllByCompanyId() {
        // Arrange
        Long companyId = 1L;
        Contract contract1 = new Contract();
        Contract contract2 = new Contract();
        List<Contract> expectedContracts = Arrays.asList(contract1, contract2);

        when(contractRepository.findByCompanySellerIdOrCompanyBuyerId(companyId, companyId)).thenReturn(expectedContracts);

        // Act
        List<Contract> actualContracts = contractService.getAllByCompanyId(companyId);

        // Assert
        assertNotNull(actualContracts);
        assertEquals(2, actualContracts.size());
        assertTrue(actualContracts.contains(contract1));
        assertTrue(actualContracts.contains(contract2));

        verify(contractRepository, times(1)).findByCompanySellerIdOrCompanyBuyerId(companyId, companyId);
    }

    @Test
    public void testSupervisorAccept_WhenAlreadyAccepted() {
        // Arrange
        Long contractId = 1L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);
        dto.setComment("Accepted");

        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.ACCEPTED);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        // Act
        boolean result = contractService.supervisorAccept(dto);

        // Assert
        assertTrue(result);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void testSupervisorAccept_WhenAlreadyDeclined() {
        // Arrange
        Long contractId = 1L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);
        dto.setComment("Declined");

        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.DECLINED);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        // Act
        boolean result = contractService.supervisorAccept(dto);

        // Assert
        assertFalse(result);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void testSupervisorAccept_WhenPending() {
        // Arrange
        Long contractId = 1L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);
        dto.setComment("Accepted");

        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.PROCESSING);
        contract.setPrice(BigDecimal.valueOf(1000));
        contract.setAmount(10);
        contract.setTicker("ABC");
        contract.setUserBuyerId(2L);
        contract.setCompanyBuyerId(3L);
        contract.setUserSellerId(4L);
        contract.setCompanySellerId(5L);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        // Act
        boolean result = contractService.supervisorAccept(dto);

        // Assert
        assertTrue(result);
        assertEquals(BankCertificate.ACCEPTED, contract.getBankCertificate());
        assertEquals("Accepted", contract.getComment());

        BigDecimal price = contract.getPrice().divide(BigDecimal.valueOf(contract.getAmount()), BigDecimal.ROUND_HALF_UP);

        verify(myStockService, times(1)).addAmountToMyStock("ABC", 10, 2L, 3L, price.doubleValue());
        verify(myStockService, times(1)).removeAmountFromMyStock("ABC", 10, 4L, 5L);
        verify(myStockService, times(1)).calculateTaxForSellStock(5L, 4L, "ABC", 10, price.doubleValue());
        verify(contractRepository, times(1)).save(contract);

        verify(bankServiceClient, times(1)).otcBankTransaction(any(CompanyOtcDto.class));
        verify(bankServiceClient, never()).otcUserTransaction(any(UserOtcDto.class));
    }

    @Test
    public void testSupervisorAccept_WhenUserSellerIdNotNull() {
        // Arrange
        Long contractId = 1L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);
        dto.setComment("Accepted");

        Contract contract = new Contract();
        contract.setBankCertificate(BankCertificate.PROCESSING);
        contract.setPrice(BigDecimal.valueOf(1000.0));
        contract.setUserBuyerId(2L);
        contract.setUserSellerId(4L);
        contract.setAmount(10);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        // Act
        boolean result = contractService.supervisorAccept(dto);

        // Assert
        assertTrue(result);
        assertEquals(BankCertificate.ACCEPTED, contract.getBankCertificate());
        assertEquals("Accepted", contract.getComment());

        BigDecimal amount = BigDecimal.valueOf(contract.getPrice().doubleValue());
        UserOtcDto expectedUserOtcDto = new UserOtcDto();
        expectedUserOtcDto.setAmount(amount.doubleValue());
        expectedUserOtcDto.setUserFromId(contract.getUserBuyerId());
        expectedUserOtcDto.setUserToId(contract.getUserSellerId());

        verify(bankServiceClient, times(1)).otcUserTransaction(expectedUserOtcDto);
    }
}









