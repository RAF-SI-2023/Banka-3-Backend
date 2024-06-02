package rs.edu.raf.exchangeservice.service.myListingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    private static final double BUSHEL = 6.5;
    private static final double POUND = 1.2;
    private static final double BOARD_FEET = 0.5;
    private static final double BARREL = 50.0;
    private static final double GALLON = 3.5;
    private static final double TROY_OUNCE = 500.0;
    private static final double METRIC_TON = 100.0;

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
    void testSellMyFuture() {
        // Arrange
        BuyFutureDto buyFutureDto = new BuyFutureDto();
        buyFutureDto.setCompanyId(123L);
        buyFutureDto.setFutureId(456L);

        MyFuture myFuture = new MyFuture();
        myFuture.setContractName("Corn");
        myFuture.setContractSize(5000);
        myFuture.setContractUnit("Bushel");

        when(myFutureRepository.findByMyFutureId(buyFutureDto.getFutureId())).thenReturn(myFuture);

        // Act
        FutureOrderSell futureOrderSell = myFutureService.sellMyFuture(buyFutureDto);

        // Assert
        assertEquals(buyFutureDto.getCompanyId(), futureOrderSell.getCompanyId());
        assertEquals(myFuture.getContractName(), futureOrderSell.getContractName());
        assertEquals(OrderStatus.PROCESSING, futureOrderSell.getStatus());
        assertEquals(BUSHEL * myFuture.getContractSize(), futureOrderSell.getPrice()); // Trebate zameniti BUSHEL, POUND itd. sa odgovarajućim vrednostima
    }
}