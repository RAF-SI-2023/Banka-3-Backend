package rs.edu.raf.exchangeservice.service.myListingService;

import io.cucumber.plugin.event.EventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyFuture;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyFutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderSellRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MyFutureSerivceTest {
    @Mock
    private MyFutureRepository myFutureRepository;

    @InjectMocks
    private MyFutureSerivce myFutureService;

    @Mock
    private FutureRepository futureRepository;

    @Mock
    private FutureOrderSellRepository futureOrderSellRepository;

    @Mock
    private BankServiceClient bankServiceClient;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    @Test
    void testFindAllForOtcBuy() {
        // Arrange
        Long companyId = 123L;
        MyFuture myFuture1 = new MyFuture();
        myFuture1.setMyFutureId(1L);
        myFuture1.setCompanyId(456L);
        MyFuture myFuture2 = new MyFuture();
        myFuture2.setMyFutureId(2L);
        myFuture2.setCompanyId(789L);

        List<MyFuture> expectedMyFutures = Arrays.asList(myFuture1, myFuture2);

        when(myFutureRepository.findByIsPublicTrueAndCompanyIdNot(companyId)).thenReturn(expectedMyFutures);

        // Act
        List<MyFuture> actualMyFutures = myFutureService.findAllForOtcBuy(companyId);

        // Assert
        assertEquals(expectedMyFutures, actualMyFutures);
    }

    @Test
    void testAddMyFuture() {
        // Arrange
        MyFuture myFuture = new MyFuture();
        // Dodajte podatke u myFuture po potrebi

        // Act
        myFutureService.addMyFuture(myFuture);

        // Assert
        verify(myFutureRepository, times(1)).save(myFuture);
    }

    @Test
    void testFindAllByCompanyId() {
        // Arrange
        Long companyId = 123L;
        MyFuture myFuture1 = new MyFuture();
        myFuture1.setMyFutureId(1L);
        myFuture1.setCompanyId(companyId);
        MyFuture myFuture2 = new MyFuture();
        myFuture2.setMyFutureId(2L);
        myFuture2.setCompanyId(companyId);

        List<MyFuture> expectedMyFutures = Arrays.asList(myFuture1, myFuture2);

        when(myFutureRepository.findAllByCompanyId(companyId)).thenReturn(expectedMyFutures);

        // Act
        List<MyFuture> actualMyFutures = myFutureService.findAllByCompanyId(companyId);

        // Assert
        assertEquals(expectedMyFutures, actualMyFutures);
    }

    @Test
    void testMakeFuturePublic() {
        // Arrange
        Long myFutureId = 123L;
        MyFuture myFuture = new MyFuture();
        myFuture.setMyFutureId(myFutureId);
        myFuture.setIsPublic(false); // Postavljamo da nije javno

        when(myFutureRepository.findByMyFutureId(myFutureId)).thenReturn(myFuture);

        // Act
        MyFuture updatedMyFuture = myFutureService.makeFuturePublic(myFutureId);

        // Assert
        assertTrue(updatedMyFuture.getIsPublic()); // Proveravamo da li je sada javno
        verify(myFutureRepository, times(1)).save(myFuture); // Proveravamo da li je save pozvan tačno jednom
    }

    @Test
    void testMakeFuturePrivate() {
        // Arrange
        Long myFutureId = 123L;
        MyFuture myFuture = new MyFuture();
        myFuture.setMyFutureId(myFutureId);
        myFuture.setIsPublic(true); // Postavljamo da je javno

        when(myFutureRepository.findByMyFutureId(myFutureId)).thenReturn(myFuture);

        // Act
        MyFuture updatedMyFuture = myFutureService.makeFuturePrivate(myFutureId);

        // Assert
        assertFalse(updatedMyFuture.getIsPublic()); // Proveravamo da li je sada privatno
        verify(myFutureRepository, times(1)).save(myFuture); // Proveravamo da li je save pozvan tačno jednom
    }

    @Test
    public void testSellMyFuture() {
        // Priprema
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        MyFuture dummyMyFuture = createDummyMyFuture();

        given(myFutureRepository.findByMyFutureId(1L)).willReturn(dummyMyFuture);

        // Poziv metode koju testiramo
        FutureOrderSell result = myFutureService.sellMyFuture(buyFutureDto);

        // Provera
        assertEquals(buyFutureDto.getCompanyId(), result.getCompanyId());
        assertEquals(OrderStatus.PROCESSING, result.getStatus());
        // Dodatne provere po potrebi
    }

    // Metoda za kreiranje dummy objekta BuyFutureDto
    private BuyFutureDto createDummyBuyFutureDto() {
        BuyFutureDto buyFutureDto = new BuyFutureDto();
        buyFutureDto.setCompanyId(1L);
        buyFutureDto.setFutureId(1L);
        // Postavite ostale podatke po potrebi
        return buyFutureDto;
    }

    // Metoda za kreiranje dummy objekta MyFuture
    private MyFuture createDummyMyFuture() {
        MyFuture dummyMyFuture = new MyFuture();
        dummyMyFuture.setMyFutureId(1L);
        dummyMyFuture.setContractName("Dummy Contract");
        dummyMyFuture.setPrice(100.0);
        // Postavite ostale podatke po potrebi
        return dummyMyFuture;
    }
}