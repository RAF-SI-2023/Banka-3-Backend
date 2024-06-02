package rs.edu.raf.exchangeservice.service.myListingService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyForexDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyForex;
import rs.edu.raf.exchangeservice.domain.model.order.ForexOrder;
import rs.edu.raf.exchangeservice.repository.listingRepository.ForexRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyForexRepositroy;
import rs.edu.raf.exchangeservice.repository.orderRepository.ForexOrderRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyForexServiceTest {
    @InjectMocks
    private MyForexService myForexService;

    @Mock
    private ForexOrderRepository forexOrderRepository;

    @Mock
    private MyForexRepositroy myForexRepositroy;

    @Mock
    private ForexRepository forexRepository;

    @Mock
    private BankServiceClient bankServiceClient;


    @Test
    void testFindAll() {
        // Priprema testnih podataka
        List<ForexOrder> expectedForexOrderList = Arrays.asList(new ForexOrder(), new ForexOrder());
        when(forexOrderRepository.findAll()).thenAnswer(invocation -> expectedForexOrderList);

        // Poziv metode findAll
        List<ForexOrder> actualForexOrderList = myForexService.findAll();

        // Provera da li je findAll metoda pozvana na repozitorijumu
        verify(forexOrderRepository, times(1)).findAll();

        // Provera da li su povratna lista i očekivana lista jednake
        assertEquals(expectedForexOrderList, actualForexOrderList);
    }

    @Test
    void testFindAllByCompanyId() {
        // Priprema testnih podataka
        Long companyId = 123L;
        List<ForexOrder> expectedForexOrderList = Arrays.asList(new ForexOrder(), new ForexOrder());
        when(forexOrderRepository.findAllByCompanyId(companyId)).thenReturn(expectedForexOrderList);

        // Poziv metode findAllByCompanyId
        List<ForexOrder> actualForexOrderList = myForexService.findAllByCompanyId(companyId);

        // Provera da li je findAllByCompanyId metoda pozvana na repozitorijumu sa odgovarajućim companyId parametrom
        verify(forexOrderRepository, times(1)).findAllByCompanyId(companyId);

        // Provera da li su povratna lista i očekivana lista jednake
        assertEquals(expectedForexOrderList, actualForexOrderList);
    }

    @Test
    void testFindAllMyForexByCompanyId() {
        // Priprema testnih podataka
        Long companyId = 123L;
        List<MyForex> expectedMyForexList = Arrays.asList(new MyForex(), new MyForex());
        when(myForexRepositroy.findAllByCompanyId(companyId)).thenReturn(expectedMyForexList);

        // Poziv metode findAllMyForexByCompanyId
        List<MyForex> actualMyForexList = myForexService.findAllMyForexByCompanyId(companyId);

        // Provera da li je findAllMyForexByCompanyId metoda pozvana na repozitorijumu sa odgovarajućim companyId parametrom
        verify(myForexRepositroy, times(1)).findAllByCompanyId(companyId);

        // Provera da li su povratna lista i očekivana lista jednake
        assertEquals(expectedMyForexList, actualMyForexList);
    }

    @Test
    void testAddAmount_NewMyForex() {
        // Priprema testnih podataka
        Long companyId = 123L;
        String quoteCurrency = "USD";
        Double amount = 100.0;
        MyForex myForex = null; // Simulacija da ne postoji zapis za datu kompaniju i valutu
        when(myForexRepositroy.findByCompanyIdAndQuoteCurrency(companyId, quoteCurrency)).thenReturn(myForex);

        // Poziv metode addAmount
        myForexService.addAmount(companyId, quoteCurrency, amount);

        // Provera da li je save metoda pozvana na repozitorijumu sa odgovarajućim MyForex objektom
        verify(myForexRepositroy, times(1)).save(any(MyForex.class));
    }

    @Test
    void testAddAmount_ExistingMyForex() {
        // Priprema testnih podataka
        Long companyId = 123L;
        String quoteCurrency = "USD";
        Double amount = 100.0;
        MyForex existingMyForex = new MyForex();
        existingMyForex.setCompanyId(companyId);
        existingMyForex.setQuoteCurrency(quoteCurrency);
        existingMyForex.setAmount(50.0); // Simulacija da već postoji zapis za datu kompaniju i valutu
        when(myForexRepositroy.findByCompanyIdAndQuoteCurrency(companyId, quoteCurrency)).thenReturn(existingMyForex);

        // Poziv metode addAmount
        myForexService.addAmount(companyId, quoteCurrency, amount);

        // Provera da li je save metoda pozvana na repozitorijumu sa odgovarajućim MyForex objektom
        verify(myForexRepositroy, times(1)).save(existingMyForex);
    }
    @Test
    void testBuyForex() {
        // Priprema testnih podataka
        BuyForexDto buyForexDto = new BuyForexDto();
        buyForexDto.setCompanyId(123L);
        buyForexDto.setForexId(456L);
        buyForexDto.setAmount(100.0);

        Forex forex = new Forex();
        forex.setQuoteCurrency("USD"); // Simulacija vraćanja Forex objekta iz repozitorijuma
        when(forexRepository.findByForexId(buyForexDto.getForexId())).thenReturn(forex);

        // Poziv metode buyForex
        ForexOrder result = myForexService.buyForex(buyForexDto);

        // Provera da li je postavljeno pravilno
        assertEquals(buyForexDto.getCompanyId(), result.getCompanyId());
        assertEquals(forex.getQuoteCurrency(), result.getQuoteCurrency());
        assertEquals(OrderStatus.PROCESSING, result.getStatus());
        assertEquals(buyForexDto.getAmount(), result.getAmount());

        // Provera da li je forexOrder dodat u listu ordersToBuy
        assertEquals(1, myForexService.ordersToBuy.size());
        assertEquals(result, myForexService.ordersToBuy.get(0));
    }
    @Test
    void testExecuteOrders() {
        // Priprema testnih podataka
        CopyOnWriteArrayList<ForexOrder> ordersToBuy = new CopyOnWriteArrayList<>();
        ForexOrder forexOrder = new ForexOrder();
        forexOrder.setCompanyId(123L);
        forexOrder.setQuoteCurrency("USD");
        forexOrder.setAmount(100.0);
        ordersToBuy.add(forexOrder);

        // Postavite mock ponašanje
        myForexService.ordersToBuy = ordersToBuy; // Postavlja simulirane podatke u ordersToBuy
        when(forexRepository.findByQuoteCurrency(anyString())).thenReturn(new Forex()); // Simulira vraćanje Forex objekta

        // Poziv metode executeOrders
        myForexService.executeOrders();

        // Provera da li su odgovarajuće metode pozvane
        verify(forexRepository, times(1)).findByQuoteCurrency(anyString());
        verify(bankServiceClient, times(1)).stockBuyTransaction(any(BankTransactionDto.class));
        verify(forexOrderRepository, times(1)).save(any(ForexOrder.class));

        // Provera da li je forexOrder status promenjen na FINISHED
        assertEquals(OrderStatus.FINISHED, forexOrder.getStatus());

        // Provera da li je forexOrder uklonjen iz ordersToBuy
        assertTrue(myForexService.ordersToBuy.isEmpty());
    }
}