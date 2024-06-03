package rs.edu.raf.exchangeservice.service.orderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.order.FutureOrder;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.FutureOrderRepository;

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
    private static final double BUSHEL=6.5;
    private static final double POUND=1.2;
    private static final double BOARD_FEET=0.5;
    private static final double BARREL=50.0;
    private static final double GALLON=3.5;
    private static final double TROY_OUNCE=500.0;
    private static final double METRIC_TON=100.0;


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

    @Test
    public void testBuyFuture() {
        // Pripremite DTO objekat za testiranje
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();

        // Pripremite dummy objekte Future i FutureOrder koji će biti potrebni za testiranje
        Future dummyFuture = createDummyFuture();
        FutureOrder dummyFutureOrder = createDummyFutureOrder();

        // Postavite ponašanje mock-ovanih repozitorijuma
        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenReturn(dummyFutureOrder);

        // Pozovite metodu koju testirate
        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        // Proverite da li je povratna vrednost metode jednaka dummy FutureOrder objektu
        assertEquals(dummyFutureOrder, result);
    }

    @Test
    public void testBuyFuture_Bushel() {
        // Pripremite DTO objekat za testiranje
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();

        // Pripremite dummy objekat Future sa CONTRACT_UNIT "BUSHEL"
        Future dummyFuture = createDummyFuture1("BUSHEL", 10);

        // Postavite ponašanje mock-ovanih repozitorijuma
        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Pozovite metodu koju testirate
        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        // Proverite da li je povratna vrednost metode postavljena pravilno
        assertEquals(BUSHEL * dummyFuture.getContractSize(), result.getPrice());
    }

    @Test
    public void testBuyFuture_Pound() {
        // Pripremite DTO objekat za testiranje
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();

        // Pripremite dummy objekat Future sa CONTRACT_UNIT "POUND"
        Future dummyFuture = createDummyFuture1("POUND", 20);

        // Postavite ponašanje mock-ovanih repozitorijuma
        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Pozovite metodu koju testirate
        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        // Proverite da li je povratna vrednost metode postavljena pravilno
        assertEquals(POUND * dummyFuture.getContractSize(), result.getPrice());
    }

    @Test
    public void testBuyFuture_BoardFeet() {
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        Future dummyFuture = createDummyFuture1("BOARD FEET", 30);

        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        assertEquals(BOARD_FEET * dummyFuture.getContractSize(), result.getPrice());
    }

    @Test
    public void testBuyFuture_Barrel() {
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        Future dummyFuture = createDummyFuture1("BARREL", 40);

        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        assertEquals(BARREL * dummyFuture.getContractSize(), result.getPrice());
    }

    @Test
    public void testBuyFuture_Gallon() {
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        Future dummyFuture = createDummyFuture1("GALLON", 50);

        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        assertEquals(GALLON * dummyFuture.getContractSize(), result.getPrice());
    }

    @Test
    public void testBuyFuture_TroyOunce() {
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        Future dummyFuture = createDummyFuture1("TROY OUNCE", 60);

        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        assertEquals(TROY_OUNCE * dummyFuture.getContractSize(), result.getPrice());
    }

    @Test
    public void testBuyFuture_MetricTon() {
        BuyFutureDto buyFutureDto = createDummyBuyFutureDto();
        Future dummyFuture = createDummyFuture1("METRIC TON", 70);

        when(futureRepository.findByFutureId(anyLong())).thenReturn(dummyFuture);
        when(futureOrderRepository.save(any(FutureOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FutureOrder result = futurOrderService.buyFuture(buyFutureDto);

        assertEquals(METRIC_TON * dummyFuture.getContractSize(), result.getPrice());
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