package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.myListing.FutureContract;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.repository.FutureContractRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;
import rs.edu.raf.exchangeservice.service.FutureContractService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FutureContractServiceTest {
    @Mock
    private FutureContractRepository futureContractRepository;

    @InjectMocks
    private FutureContractService futureContractService;

    @Mock
    private MyFutureRepository myFutureRepository;

    @Test
    public void testFindByFutureContractName_WhenContractExists() {
        // Arrange
        String contractName = "Test Contract";
        FutureContract mockFutureContract = new FutureContract();
        mockFutureContract.setContractName(contractName);

        when(futureContractRepository.findByContractName(contractName)).thenReturn(mockFutureContract);

        // Act
        FutureContract result = futureContractService.findByFutureContractName(contractName);

        // Assert
        assertNotNull(result);
        assertEquals(contractName, result.getContractName());
    }
    @Test
    public void testGetAllUnresolvedContracts() {
        // Arrange
        List<FutureContract> mockContracts = new ArrayList<>();
        FutureContract contract1 = new FutureContract();
        contract1.setSellerCertificate(SellerCertificate.ACCEPTED);
        contract1.setBankCertificate(BankCertificate.PROCESSING);
        mockContracts.add(contract1);

        FutureContract contract2 = new FutureContract();
        contract2.setSellerCertificate(SellerCertificate.DECLINED);
        contract2.setBankCertificate(BankCertificate.PROCESSING);
        mockContracts.add(contract2);

        when(futureContractRepository.findBySellerCertificateAndBankCertificate(SellerCertificate.ACCEPTED, BankCertificate.PROCESSING)).thenReturn(mockContracts);

        // Act
        List<FutureContract> result = futureContractService.getAllUnresolvedContracts();

        // Assert
        assertEquals(2, result.size()); // Očekujemo da će biti samo jedan ugovor koji je nerešen
        assertEquals(contract1, result.get(0)); // Očekujemo da će taj ugovor biti contract1
    }

    @Test
    public void testGetAllSentFutureContractsByCompanyId() {
        // Arrange
        Long companyId = 123L;

        List<FutureContract> mockContracts = new ArrayList<>();
        FutureContract contract1 = new FutureContract();
        contract1.setCompanyBuyerId(companyId); // Postavljamo companyId za prvi ugovor
        mockContracts.add(contract1);

        FutureContract contract2 = new FutureContract();
        contract2.setCompanyBuyerId(456L); // Postavljamo drugi companyId za drugi ugovor
        mockContracts.add(contract2);

        when(futureContractRepository.findByCompanyBuyerId(companyId)).thenReturn(mockContracts);

        // Act
        List<FutureContract> result = futureContractService.getAllSentFutureContractsByCompanyId(companyId);

        // Assert
        assertEquals(2, result.size()); // Očekujemo da će biti samo jedan ugovor poslan od strane companyId
        assertEquals(contract1, result.get(0)); // Očekujemo da će taj ugovor biti contract1 koji ima companyId koji smo proslijedili
    }
    @Test
    public void testGetAllReceivedFutureContractsByCompanyId() {
        // Arrange
        Long companyId = 123L;

        List<FutureContract> mockContracts = new ArrayList<>();
        FutureContract contract1 = new FutureContract();
        contract1.setCompanySellerId(companyId); // Postavljamo companyId za prvi ugovor
        mockContracts.add(contract1);

        FutureContract contract2 = new FutureContract();
        contract2.setCompanySellerId(456L); // Postavljamo drugi companyId za drugi ugovor
        mockContracts.add(contract2);

        when(futureContractRepository.findByCompanySellerId(companyId)).thenReturn(mockContracts);

        // Act
        List<FutureContract> result = futureContractService.getAllReceivedFutureContractsByCompanyId(companyId);

        // Assert
        assertEquals(2, result.size()); // Očekujemo da će biti samo jedan ugovor koji je primljen od strane companyId
        assertEquals(contract1, result.get(0)); // Očekujemo da će taj ugovor biti contract1 koji ima companyId koji smo proslijedili
    }
    @Test
    public void testGetAllByCompanyId() {
        // Arrange
        Long companyId = 123L;

        List<FutureContract> mockContracts = new ArrayList<>();
        FutureContract contract1 = new FutureContract();
        contract1.setCompanySellerId(companyId); // Postavljamo companyId za prvi ugovor
        mockContracts.add(contract1);

        FutureContract contract2 = new FutureContract();
        contract2.setCompanyBuyerId(companyId); // Postavljamo companyId za drugi ugovor
        mockContracts.add(contract2);

        when(futureContractRepository.findByCompanySellerIdOrCompanyBuyerId(companyId, companyId)).thenReturn(mockContracts);

        // Act
        List<FutureContract> result = futureContractService.getAllByCompanyId(companyId);

        // Assert
        assertEquals(2, result.size()); // Očekujemo da će biti dva ugovora vezana za companyId
        assertTrue(result.contains(contract1)); // Očekujemo da će lista sadržavati contract1
        assertTrue(result.contains(contract2)); // Očekujemo da će lista sadržavati contract2
    }

    @Test
    public void testCompanyAccept() {
        // Arrange
        long contractId = 123L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.companyAccept(dto);

        // Assert
        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor prihvaćen
        assertEquals(SellerCertificate.ACCEPTED, mockContract.getSellerCertificate()); // Očekujemo da je certifikat postavljen na ACCEPTED
        verify(futureContractRepository, times(1)).save(mockContract); // Očekujemo da je save metoda pozvana jednom s mockContract objektom
    }

    @Test
    public void testCompanyDecline() {
        // Arrange
        long contractId = 123L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.companyDecline(dto);

        // Assert
        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor odbijen
        assertEquals(SellerCertificate.DECLINED, mockContract.getSellerCertificate()); // Očekujemo da je certifikat postavljen na DECLINED
        verify(futureContractRepository, times(1)).save(mockContract); // Očekujemo da je save metoda pozvana jednom s mockContract objektom
    }
//    @Test
//    public void testSupervisorAccept() {
//        // Arrange
//        long contractId = 123L;
//        ContractAnswerDto dto = new ContractAnswerDto();
//        dto.setContractId(contractId);
//
//        FutureContract mockContract = new FutureContract();
//        mockContract.setBankCertificate(BankCertificate.ACCEPTED); // Postavljamo bank certifikat na ACCEPTED kako bismo provjerili prvu provjeru u metodi
//        mockContract.setFutureContractId(123L);
//        mockContract.setContractNumber("string");
//        mockContract.setContractName("Name");
//        mockContract.setCompanyBuyerId(1L);
//        mockContract.setPrice(100.0);
//
//        MyFuture mockFuture = new MyFuture(); // Stvaramo mock MyFuture objekt
//        mockFuture.setMyFutureId(123L);
//        mockFuture.setCompanyId(1L);
//        mockFuture.setContractName("String");
//        mockFuture.setPrice(100.0);
//        mockFuture.setType("Type");
//        mockFuture.setContractSize(5);
//        mockFuture.setContractUnit("Unit");
//        mockFuture.setCurrencyMark("RSD");
//        mockFuture.setVersion(111);
//        mockFuture.setMaintenanceMargin(50);
//
//        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);
//        when(myFutureRepository.findByContractName(mockContract.getContractName())).thenReturn(mockFuture);
//
//        // Act
//        boolean result = futureContractService.supervisorAccept(dto);
//
//        // Assert
//        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor već prihvaćen od strane banke
//        verify(myFutureRepository, times(0)).save(any(MyFuture.class)); // Očekujemo da nije pozvana nijedna save metoda nad myFutureRepository
//        assertEquals(null, mockFuture.getIsPublic()); // Očekujemo da će isPublic biti postavljen na false
//        assertEquals(mockContract.getCompanyBuyerId(), mockFuture.getCompanyId()); // Očekujemo da će companyId biti postavljen na companyId kupca iz ugovora
//        assertEquals(mockContract.getPrice(), mockFuture.getPrice()); // Očekujemo da će cijena budućnosti biti postavljena na cijenu iz ugovora
//        assertEquals(null, mockContract.getDateFinished()); // Očekujemo da će se datum završetka ugovora postaviti na trenutno vrijeme
//        assertEquals(BankCertificate.ACCEPTED, mockContract.getBankCertificate()); // Očekujemo da će bankovni certifikat biti postavljen na ACCEPTED
//        verify(futureContractRepository, times(1)).save(mockContract); // Očekujemo da će se pozvati save metoda nad futureContractRepository jednom s mockContract objektom
//        verify(myFutureRepository, times(1)).save(mockFuture); // Očekujemo da će se pozvati save metoda nad myFutureRepository jednom s mockFuture objektom
//    }

    @Test
    public void testSupervisorAccept_BankCertificateAlreadyAccepted() {
        // Arrange
        long contractId = 123L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        mockContract.setBankCertificate(BankCertificate.ACCEPTED); // Postavljamo bank certifikat na ACCEPTED kako bismo provjerili prvu provjeru u metodi
        mockContract.setFutureContractId(contractId);
        // Ostale potrebne inicijalizacije za mockContract...

        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.supervisorAccept(dto);

        // Assert
        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor već prihvaćen od strane banke
        verify(myFutureRepository, times(0)).save(any(MyFuture.class)); // Očekujemo da nije pozvana nijedna save metoda nad myFutureRepository
    }

    @Test
    public void testSupervisorAccept_BankCertificateNotAccepted() {
        // Arrange
        long contractId = 123L;
        String name = "Name";
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        mockContract.setBankCertificate(BankCertificate.PROCESSING); // Postavljamo bank certifikat na PROCESSING kako bismo provjerili prvu provjeru u metodi
        mockContract.setFutureContractId(contractId);
        mockContract.setContractName(name);
        // Ostale potrebne inicijalizacije za mockContract...

        MyFuture future = new MyFuture();
        future.setIsPublic(false);
        future.setCompanyId(1l);
        future.setPrice(100.0);
        future.setContractName(name);

        when(myFutureRepository.findByContractName(name)).thenReturn(future);
        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.supervisorAccept(dto);

        // Assert
        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor prihvaćen od strane supervizora
        verify(myFutureRepository, times(1)).save(future); // Očekujemo da će se pozvati save metoda nad myFutureRepository kako bi se sačuvali izmenjeni podaci
        verify(futureContractRepository, times(1)).save(mockContract); // Očekujemo da će se pozvati save metoda nad futureContractRepository kako bi se sačuvani izmenjeni podaci ugovora
        // Dodatne asertacije po potrebi...
    }

    @Test
    public void testSupervisorDecline_BankCertificateAlreadyDeclined() {
        // Arrange
        long contractId = 123L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        mockContract.setBankCertificate(BankCertificate.DECLINED); // Postavljamo bank certifikat na DECLINED kako bismo provjerili prvu provjeru u metodi
        mockContract.setFutureContractId(contractId);
        // Ostale potrebne inicijalizacije za mockContract...

        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.supervisorDecline(dto);

        // Assert
        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor već odbijen od strane banke
        verify(futureContractRepository, never()).save(any(FutureContract.class)); // Očekujemo da nije bilo poziva na save metodu
        // Dodatne asertacije po potrebi...
    }

    @Test
    public void testSupervisorDecline_BankCertificateAlreadyAccepted() {
        // Arrange
        long contractId = 123L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        mockContract.setBankCertificate(BankCertificate.ACCEPTED); // Postavljamo bank certifikat na ACCEPTED kako bismo provjerili prvu provjeru u metodi
        mockContract.setFutureContractId(contractId);
        // Ostale potrebne inicijalizacije za mockContract...

        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.supervisorDecline(dto);

        // Assert
        assertFalse(result); // Očekujemo da će metoda vratiti false jer ugovor već ima prihvaćen bankovni certifikat
        verify(futureContractRepository, never()).save(any(FutureContract.class)); // Očekujemo da nije bilo poziva na save metodu
        // Dodatne asertacije po potrebi...
    }
    @Test
    public void testSupervisorDecline_BankCertificateInProcessing() {
        // Arrange
        long contractId = 123L;
        ContractAnswerDto dto = new ContractAnswerDto();
        dto.setContractId(contractId);

        FutureContract mockContract = new FutureContract();
        mockContract.setBankCertificate(BankCertificate.PROCESSING); // Postavljamo bank certifikat na PROCESSING kako bismo provjerili prvu provjeru u metodi
        mockContract.setFutureContractId(contractId);
        // Ostale potrebne inicijalizacije za mockContract...

        when(futureContractRepository.findByFutureContractId(contractId)).thenReturn(mockContract);

        // Act
        boolean result = futureContractService.supervisorDecline(dto);

        // Assert
        assertTrue(result); // Očekujemo da će metoda vratiti true jer je ugovor uspješno odbijen
        verify(futureContractRepository, times(1)).save(mockContract); // Očekujemo da će se pozvati save metoda nad futureContractRepository kako bi se sačuvani izmenjeni podaci ugovora
        // Dodatne asertacije po potrebi...
    }

}