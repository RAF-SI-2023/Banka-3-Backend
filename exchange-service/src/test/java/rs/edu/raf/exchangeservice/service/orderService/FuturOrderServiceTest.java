package rs.edu.raf.exchangeservice.service.orderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyFutureSerivce;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuturOrderServiceTest {
    @InjectMocks
    private FuturOrderService futurOrderService;
    @Mock
    private FutureOrderRepository futureOrderRepository;
    @Mock
    private FutureRepository futureRepository;
    @Mock
    private MyFutureSerivce myFutureSerivce;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private BankServiceClient bankServiceClient;


    @Test
    public void testBuyFuture() {
        // Priprema
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        Future dummyFuture = createDummyFuture();

        when(futureRepository.findByFutureId(1L)).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> {
            FutureOrder arg = invocation.getArgument(0);
            arg.setFutureOrderId(1L); // Postavljanje ID-a za simulaciju snimanja u bazu
            return arg;
        });

        // Poziv metode koju testiramo
        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        // Provera
        assertNotNull(result);
        assertEquals(buyFutureDto.getCompanyId(), result.getCompanyId());
        assertEquals(OrderStatus.PROCESSING, result.getStatus());
        // Dodajte dodatne provere po potrebi
    }
    @Test
    public void testFindAll() {
        // Pripremite listu dummy objekata FutureOrder
        List<FutureOrder> dummyFutureOrders = createDummyFutureOrders();

        // Postavite ponašanje mock-ovanog futureOrderRepository
        given(futureOrderRepository.findAll()).willReturn(dummyFutureOrders);

        // Pozovite metodu koju testirate
        List<FutureOrder> result = futurOrderService.findAll();

        // Proverite da li je povratna vrednost jednaka dummy listi
        assertEquals(dummyFutureOrders, result);
    }


    // Metoda za kreiranje dummy objekta Future sa specificiranom CONTRACT_UNIT vrednošću
    private Future createDummyFuture1(String contractUnit, int contractSize) {
        Future dummyFuture = new Future();
        dummyFuture.setFutureId(1L);
        dummyFuture.setContractName("Dummy Contract");
        dummyFuture.setContractUnit(contractUnit);
        dummyFuture.setContractSize(contractSize);
        // Postavite ostale podatke po potrebi
        return dummyFuture;
    }

    // Metoda za kreiranje dummy objekta BuyFutureDto
    private BuyFutureDto createDummyBuyFutureDto() {
        BuyFutureDto buyFutureDto = new BuyFutureDto();
        buyFutureDto.setCompanyId(1L);
        buyFutureDto.setFutureId(1L);
        // Postavite ostale podatke po potrebi
        return buyFutureDto;
    }

    // Metoda za kreiranje dummy objekta Future
    private Future createDummyFuture() {
        Future dummyFuture = new Future();
        dummyFuture.setFutureId(1L);
        dummyFuture.setContractUnit("Unit");
        dummyFuture.setPrice(100.0);
        dummyFuture.setType("Type");
        dummyFuture.setContractSize(5);
        //dummyFuture.setContractName("Name");
        // Postavite ostale podatke po potrebi
        return dummyFuture;
    }

    // Metoda za kreiranje dummy objekta FutureOrder
    private FutureOrder createDummyFutureOrder() {
        FutureOrder dummyFutureOrder = new FutureOrder();
        dummyFutureOrder.setCompanyId(1L);
        dummyFutureOrder.setStatus(OrderStatus.PROCESSING);
        // Postavite ostale podatke po potrebi
        return dummyFutureOrder;
    }

    // Metoda za kreiranje dummy objekata FutureOrder
    private List<FutureOrder> createDummyFutureOrders() {
        List<FutureOrder> dummyFutureOrders = new ArrayList<>();

        // Dodajte nekoliko dummy objekata u listu
        FutureOrder futureOrder1 = new FutureOrder();
        futureOrder1.setFutureOrderId(1L);
        // Postavite ostale podatke po potrebi
        dummyFutureOrders.add(futureOrder1);

        FutureOrder futureOrder2 = new FutureOrder();
        futureOrder2.setFutureOrderId(2L);
        // Postavite ostale podatke po potrebi
        dummyFutureOrders.add(futureOrder2);

        // Dodajte koliko god dummy objekata želite

        return dummyFutureOrders;
    }
}