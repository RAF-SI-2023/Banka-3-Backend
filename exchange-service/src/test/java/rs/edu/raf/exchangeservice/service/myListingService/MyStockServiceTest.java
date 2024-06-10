package rs.edu.raf.exchangeservice.service.myListingService;

import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.StockUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.MakePublicStockDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.model.ProfitStock;
import rs.edu.raf.exchangeservice.domain.model.TaxStock;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.ProfitStockRepositorty;
import rs.edu.raf.exchangeservice.repository.TaxStockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;
import rs.edu.raf.exchangeservice.service.listingService.TickerService;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyStockServiceTest {
    @Mock
    private MyStockRepository myStockRepository;

    @Mock
    private ProfitStockRepositorty profitStockRepositorty;

    @Mock
    private TaxStockRepository taxStockRepository;
    @Mock
    private StockOrderSellRepository stockOrderSellRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    @InjectMocks
    private MyStockService myStockService;

    @Test
    public void testRemoveAmountFromMyStock() {
        // Priprema testnih podataka
        String ticker = "AAPL";
        Integer amount = 5;
        Long userId = 1L;
        Long companyId = 1L; // Ako koristite companyId, postavite vrednost

        MyStock myStock = new MyStock();// Kreirajte MyStock objekat po potrebi
        myStock.setPublicAmount(50);
        myStock.setAmount(100);
        myStock.setPrivateAmount(50);
        myStock.setCompanyId(companyId);

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(ticker, userId)).thenReturn(myStock);

        // Poziv metode koju testiramo
        myStockService.removeAmountFromMyStock(ticker, amount, userId, companyId);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(ticker, userId);
        verify(myStockRepository, times(1)).save(myStock); // Proverava se da li je save metoda pozvana tačno jednom
    }

    @Test
    public void testRemoveAmountFromMyStock_WithCompanyId() {
        // Priprema testnih podataka
        String ticker = "AAPL";
        Integer amount = 5;
        Long userId = null; // Postavite vrednost ako koristite userId
        Long companyId = 1L;

        MyStock myStock = new MyStock();// Kreirajte MyStock objekat po potrebi
        myStock.setPublicAmount(50);
        myStock.setAmount(100);
        myStock.setPrivateAmount(50);

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndCompanyId(ticker, companyId)).thenReturn(myStock);

        // Poziv metode koju testiramo
        myStockService.removeAmountFromMyStock(ticker, amount, userId, companyId);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndCompanyId(ticker, companyId);
        verify(myStockRepository, times(1)).save(myStock); // Proverava se da li je save metoda pozvana tačno jednom
    }

    @Test
    public void testCalculateTaxForSellStock_WithUserId() {
        // Priprema testnih podataka
        String ticker = "AAPL";
        Integer sellAmount = 10;
        Double sellPrice = 150.0;
        Long userId = 1L;
        Long companyId = null; // Postavite vrednost ako koristite companyId

        MyStock myStock = new MyStock();
        myStock.setMinimumPrice(100.0);
        // Postavite ostale potrebne vrednosti

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(ticker, userId)).thenReturn(myStock);

        // Poziv metode koju testiramo
        myStockService.calculateTaxForSellStock(companyId, userId, ticker, sellAmount, sellPrice);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(ticker, userId);
        verify(taxStockRepository, times(1)).save(any(TaxStock.class)); // Proverava se da li je save metoda pozvana tačno jednom
    }

    @Test
    public void testCalculateTaxForSellStock_WithCompanyId() {
        // Priprema testnih podataka
        String ticker = "AAPL";
        Integer sellAmount = 10;
        Double sellPrice = 150.0;
        Long userId = null; // Postavite vrednost ako koristite userId
        Long companyId = 1L;

        MyStock myStock = new MyStock();
        myStock.setMinimumPrice(100.0);
        // Postavite ostale potrebne vrednosti

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndCompanyId(ticker, companyId)).thenReturn(myStock);

        // Poziv metode koju testiramo
        myStockService.calculateTaxForSellStock(companyId, userId, ticker, sellAmount, sellPrice);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndCompanyId(ticker, companyId);
        verify(taxStockRepository, times(1)).save(any(TaxStock.class)); // Proverava se da li je save metoda pozvana tačno jednom
    }

    @Test
    public void testAddProfitForEmployee() {
        // Priprema testnih podataka
        Long employeeId = 1L;
        Double amount = 500.0;

        // Poziv metode koju testiramo
        myStockService.addProfitForEmployee(employeeId, amount);

        // Provera da li je odgovarajuća metoda repozitorijuma pozvana
        verify(profitStockRepositorty, times(1)).save(any(ProfitStock.class)); // Proverava se da li je save metoda pozvana tačno jednom
    }

    @Test
    public void testMakeCompanyStockPublic() {
        // Priprema testnih podataka
        MakePublicStockDto makePublicStockDto = new MakePublicStockDto();
        makePublicStockDto.setTicker("AAPL");
        makePublicStockDto.setOwnerId(1L);
        makePublicStockDto.setAmount(50);

        MyStock myStock = new MyStock();
        myStock.setAmount(100); // Postavite odgovarajuće vrednosti za MyStock objekat

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndCompanyId(makePublicStockDto.getTicker(), makePublicStockDto.getOwnerId())).thenReturn(myStock);

        // Poziv metode koju testiramo
        MyStock result = myStockService.makeCompanyStockPublic(makePublicStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndCompanyId(makePublicStockDto.getTicker(), makePublicStockDto.getOwnerId());
        verify(myStockRepository, times(1)).save(myStock); // Proverava se da li je save metoda pozvana tačno jednom

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(makePublicStockDto.getAmount(), result.getPublicAmount());
        assertEquals(50, result.getPrivateAmount()); // Postavite odgovarajuću vrednost za privatnu količinu
    }

    @Test
    public void testMakeUserStockPublic() {
        // Priprema testnih podataka
        MakePublicStockDto makePublicStockDto = new MakePublicStockDto();
        makePublicStockDto.setTicker("AAPL");
        makePublicStockDto.setOwnerId(1L);
        makePublicStockDto.setAmount(50);

        MyStock myStock = new MyStock();
        myStock.setAmount(100); // Postavite odgovarajuće vrednosti za MyStock objekat

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(makePublicStockDto.getTicker(), makePublicStockDto.getOwnerId())).thenReturn(myStock);

        // Poziv metode koju testiramo
        MyStock result = myStockService.makeUserStockPublic(makePublicStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(makePublicStockDto.getTicker(), makePublicStockDto.getOwnerId());
        verify(myStockRepository, times(1)).save(myStock); // Proverava se da li je save metoda pozvana tačno jednom

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(makePublicStockDto.getAmount(), result.getPublicAmount());
        assertEquals(50, result.getPrivateAmount()); // Postavite odgovarajuću vrednost za privatnu količinu
    }

    @Test
    public void testGetAll() {
        // Priprema testnih podataka
        List<MyStock> mockStockList = new ArrayList<>();
        mockStockList.add(new MyStock()); // Dodajte više MyStock objekata po potrebi
        mockStockList.add(new MyStock());
        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findAll()).thenReturn(mockStockList);

        // Poziv metode koju testiramo
        List<MyStock> result = myStockService.getAll();

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(mockStockList.size(), result.size());
    }
    @Test
    public void testGetAllForCompany() {
        // Priprema testnih podataka
        Long companyId = 123L; // Postavite odgovarajuću vrednost za companyId

        List<MyStock> mockStockList = new ArrayList<>();
        mockStockList.add(new MyStock()); // Dodajte više MyStock objekata po potrebi
        mockStockList.add(new MyStock());
        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findAllByCompanyId(companyId)).thenReturn(mockStockList);

        // Poziv metode koju testiramo
        List<MyStock> result = myStockService.getAllForCompany(companyId);

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(mockStockList.size(), result.size()); // Proverava se da li je povratna lista iste veličine kao i mock-ovana lista
        // Dodatne provere po potrebi, kao što je provera jednakosti svakog elementa
    }
    @Test
    public void testGetAllForUser() {
        // Priprema testnih podataka
        Long userId = 456L; // Postavite odgovarajuću vrednost za userId

        List<MyStock> mockStockList = new ArrayList<>();
        mockStockList.add(new MyStock()); // Dodajte više MyStock objekata po potrebi
        mockStockList.add(new MyStock());
        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findAllByUserId(userId)).thenReturn(mockStockList);

        // Poziv metode koju testiramo
        List<MyStock> result = myStockService.getAllForUser(userId);

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(mockStockList.size(), result.size()); // Proverava se da li je povratna lista iste veličine kao i mock-ovana lista
        // Dodatne provere po potrebi, kao što je provera jednakosti svakog elementa
    }

    @Test
    public void testGetAllForUserOtcBuy() {
        // Priprema testnih podataka
        Long userId = 123L; // Postavite odgovarajuću vrednost za userId

        List<MyStock> mockStockList = new ArrayList<>();
        mockStockList.add(new MyStock()); // Dodajte više MyStock objekata po potrebi
        mockStockList.add(new MyStock());
        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByUserIdIsNotNullAndCompanyIdIsNullAndPublicAmountGreaterThanAndUserIdNot(0, userId)).thenReturn(mockStockList);

        // Poziv metode koju testiramo
        List<MyStock> result = myStockService.getAllForUserOtcBuy(userId);

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(mockStockList.size(), result.size()); // Proverava se da li je povratna lista iste veličine kao i mock-ovana lista
        // Dodatne provere po potrebi
    }

    @Test
    public void testGetAllForCompanyOtcBuy() {
        // Priprema testnih podataka
        Long companyId = 456L; // Postavite odgovarajuću vrednost za companyId

        List<MyStock> mockStockList = new ArrayList<>();
        mockStockList.add(new MyStock()); // Dodajte više MyStock objekata po potrebi
        mockStockList.add(new MyStock());
        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByCompanyIdIsNotNullAndUserIdIsNullAndPublicAmountGreaterThanAndCompanyIdNot(0, companyId)).thenReturn(mockStockList);

        // Poziv metode koju testiramo
        List<MyStock> result = myStockService.getAllForCompanyOtcBuy(companyId);

        // Provera povratne vrednosti
        assertNotNull(result);
        assertEquals(mockStockList.size(), result.size()); // Proverava se da li je povratna lista iste veličine kao i mock-ovana lista
        // Dodatne provere po potrebi
    }
    @Test
    public void testSellStock() {
        // Priprema testnih podataka
        BuySellStockDto sellStockDto = new BuySellStockDto(); // Postavite odgovarajuće vrednosti za dto objekat
        sellStockDto.setAmount(150);
        sellStockDto.setStopValue(200.0);
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setStopValue(0.0);
        sellStockDto.setUserId(1l);
        sellStockDto.setTicker("TICKER");

        MyStock myStock = new MyStock(); // Kreirajte odgovarajući MyStock objekat
        myStock.setAmount(100); // Postavite odgovarajuću količinu akcija
        myStock.setPrivateAmount(50);
        myStock.setPublicAmount(50);
        //myStock.setUserId(1L);

        StockOrderSell stockOrderSell = new StockOrderSell(); // Kreirajte odgovarajući StockOrderSell objekat
        stockOrderSell.setAmount(sellStockDto.getAmount()); // Postavite odgovarajuću količinu akcija

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(anyString(), anyLong())).thenReturn(myStock); // Postavite odgovarajuće ponašanje mock objekta
        when(stockOrderSellRepository.save(any(StockOrderSell.class))).thenReturn(stockOrderSell); // Postavite odgovarajuće ponašanje mock objekta

        // Poziv metode koju testiramo
        myStockService.sellStock(sellStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(anyString(), anyLong()); // Provera da li je metoda findByTickerAndUserId pozvana tačno jednom
        verify(stockOrderSellRepository, times(2)).save(any(StockOrderSell.class)); // Provera da li je metoda save pozvana tačno jednom sa odgovarajućim objektom

        // Dodatne provere po potrebi
    }
    @Test
    public void testSellStock1() {
        // Priprema testnih podataka
        BuySellStockDto sellStockDto = new BuySellStockDto(); // Postavite odgovarajuće vrednosti za dto objekat
        sellStockDto.setAmount(150);
        sellStockDto.setStopValue(200.0);
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setStopValue(100.0);
        sellStockDto.setUserId(1l);
        sellStockDto.setTicker("TICKER");

        MyStock myStock = new MyStock(); // Kreirajte odgovarajući MyStock objekat
        myStock.setAmount(100); // Postavite odgovarajuću količinu akcija
        myStock.setPrivateAmount(50);
        myStock.setPublicAmount(50);
        //myStock.setUserId(1L);

        StockOrderSell stockOrderSell = new StockOrderSell(); // Kreirajte odgovarajući StockOrderSell objekat
        stockOrderSell.setAmount(sellStockDto.getAmount()); // Postavite odgovarajuću količinu akcija

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(anyString(), anyLong())).thenReturn(myStock); // Postavite odgovarajuće ponašanje mock objekta
        when(stockOrderSellRepository.save(any(StockOrderSell.class))).thenReturn(stockOrderSell); // Postavite odgovarajuće ponašanje mock objekta

        // Poziv metode koju testiramo
        myStockService.sellStock(sellStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(anyString(), anyLong()); // Provera da li je metoda findByTickerAndUserId pozvana tačno jednom
        verify(stockOrderSellRepository, times(2)).save(any(StockOrderSell.class)); // Provera da li je metoda save pozvana tačno jednom sa odgovarajućim objektom

        // Dodatne provere po potrebi
    }
    @Test
    public void testSellStock2() {
        // Priprema testnih podataka
        BuySellStockDto sellStockDto = new BuySellStockDto(); // Postavite odgovarajuće vrednosti za dto objekat
        sellStockDto.setAmount(150);
        sellStockDto.setStopValue(200.0);
        sellStockDto.setLimitValue(100.0);
        sellStockDto.setStopValue(0.0);
        sellStockDto.setUserId(1l);
        sellStockDto.setTicker("TICKER");

        MyStock myStock = new MyStock(); // Kreirajte odgovarajući MyStock objekat
        myStock.setAmount(100); // Postavite odgovarajuću količinu akcija
        myStock.setPrivateAmount(50);
        myStock.setPublicAmount(50);
        //myStock.setUserId(1L);

        StockOrderSell stockOrderSell = new StockOrderSell(); // Kreirajte odgovarajući StockOrderSell objekat
        stockOrderSell.setAmount(sellStockDto.getAmount()); // Postavite odgovarajuću količinu akcija

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(anyString(), anyLong())).thenReturn(myStock); // Postavite odgovarajuće ponašanje mock objekta
        when(stockOrderSellRepository.save(any(StockOrderSell.class))).thenReturn(stockOrderSell); // Postavite odgovarajuće ponašanje mock objekta

        // Poziv metode koju testiramo
        myStockService.sellStock(sellStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(anyString(), anyLong()); // Provera da li je metoda findByTickerAndUserId pozvana tačno jednom
        verify(stockOrderSellRepository, times(2)).save(any(StockOrderSell.class)); // Provera da li je metoda save pozvana tačno jednom sa odgovarajućim objektom

        // Dodatne provere po potrebi
    }
    @Test
    public void testSellStock3() {
        // Priprema testnih podataka
        BuySellStockDto sellStockDto = new BuySellStockDto(); // Postavite odgovarajuće vrednosti za dto objekat
        sellStockDto.setAmount(150);
        sellStockDto.setStopValue(200.0);
        sellStockDto.setLimitValue(100.0);
        sellStockDto.setStopValue(100.0);
        sellStockDto.setUserId(1l);
        sellStockDto.setTicker("TICKER");

        MyStock myStock = new MyStock(); // Kreirajte odgovarajući MyStock objekat
        myStock.setAmount(100); // Postavite odgovarajuću količinu akcija
        myStock.setPrivateAmount(50);
        myStock.setPublicAmount(50);
        //myStock.setUserId(1L);

        StockOrderSell stockOrderSell = new StockOrderSell(); // Kreirajte odgovarajući StockOrderSell objekat
        stockOrderSell.setAmount(sellStockDto.getAmount()); // Postavite odgovarajuću količinu akcija

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndUserId(anyString(), anyLong())).thenReturn(myStock); // Postavite odgovarajuće ponašanje mock objekta
        when(stockOrderSellRepository.save(any(StockOrderSell.class))).thenReturn(stockOrderSell); // Postavite odgovarajuće ponašanje mock objekta

        // Poziv metode koju testiramo
        myStockService.sellStock(sellStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndUserId(anyString(), anyLong()); // Provera da li je metoda findByTickerAndUserId pozvana tačno jednom
        verify(stockOrderSellRepository, times(2)).save(any(StockOrderSell.class)); // Provera da li je metoda save pozvana tačno jednom sa odgovarajućim objektom

        // Dodatne provere po potrebi
    }
    @Test
    public void testSellStock4() {
        // Priprema testnih podataka
        BuySellStockDto sellStockDto = new BuySellStockDto(); // Postavite odgovarajuće vrednosti za dto objekat
        sellStockDto.setAmount(150);
        sellStockDto.setStopValue(200.0);
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setStopValue(0.0);
        sellStockDto.setCompanyId(2L);
        sellStockDto.setTicker("TICKER");

        MyStock myStock = new MyStock(); // Kreirajte odgovarajući MyStock objekat
        myStock.setAmount(100); // Postavite odgovarajuću količinu akcija
        myStock.setPrivateAmount(50);
        myStock.setPublicAmount(50);
        //myStock.setUserId(1L);

        StockOrderSell stockOrderSell = new StockOrderSell(); // Kreirajte odgovarajući StockOrderSell objekat
        stockOrderSell.setAmount(sellStockDto.getAmount()); // Postavite odgovarajuću količinu akcija

        // Mock-ovanje ponašanja repozitorijuma
        when(myStockRepository.findByTickerAndCompanyId(anyString(), anyLong())).thenReturn(myStock); // Postavite odgovarajuće ponašanje mock objekta
        when(stockOrderSellRepository.save(any(StockOrderSell.class))).thenReturn(stockOrderSell); // Postavite odgovarajuće ponašanje mock objekta

        // Poziv metode koju testiramo
        myStockService.sellStock(sellStockDto);

        // Provera da li su odgovarajuće metode repozitorijuma pozvane
        verify(myStockRepository, times(1)).findByTickerAndCompanyId(anyString(), anyLong()); // Provera da li je metoda findByTickerAndUserId pozvana tačno jednom
        verify(stockOrderSellRepository, times(2)).save(any(StockOrderSell.class)); // Provera da li je metoda save pozvana tačno jednom sa odgovarajućim objektom

        // Dodatne provere po potrebi
    }

    @Test
    public void testExecuteTask_WhenOrdersToSellIsEmpty() {
        // Postavite scenarij gde je ordersToSell lista prazna

        myStockService.ordersToSell = new CopyOnWriteArrayList<>();


        // Pozovite metodu executeTask
        myStockService.executeTask();

        // Proverite da li su metode repozitorijuma pozvane kako je očekivano
        verify(stockOrderSellRepository, never()).save(any(StockOrderSell.class));
    }
    @Test
    public void getAll_ReturnsAllMyStocks() {
        MyStock myStock1 = new MyStock();
        MyStock myStock2 = new MyStock();
        when(myStockRepository.findAll()).thenReturn(Arrays.asList(myStock1, myStock2));

        List<MyStock> result = myStockService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    public void getAll_ReturnsEmptyListWhenNoMyStocks() {
        when(myStockRepository.findAll()).thenReturn(Collections.emptyList());

        List<MyStock> result = myStockService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testExecuteTask_StopOrder_TypeChangeToMarket() {
        // Setup
        // Mocking ordersToSell list with a STOP order
        StockOrderSell stopOrder = new StockOrderSell();
        stopOrder.setType(OrderType.STOP);
        stopOrder.setTicker("AAPL");
        stopOrder.setAmountLeft(10);
        stopOrder.setStopValue(100.0); // Setting the stop value
        myStockService.ordersToSell.add(stopOrder);

        // Mocking a stock with bid price below the stop value
        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setBid(90.0); // Assuming current price is below the stop value
        when(stockRepository.findByTicker("AAPL")).thenReturn(Optional.of(stock));

        // Execution
        myStockService.executeTask();

        // Verification
        // Verify that the order type is changed to MARKET
        assertEquals(OrderType.MARKET, stopOrder.getType());
        // Verify that the order is not removed from ordersToSell list
        assertTrue(myStockService.ordersToSell.contains(stopOrder));
        // Verify that the stock order is saved in the repository after type change
        verify(stockOrderSellRepository, times(1)).save(stopOrder);
    }
    @Test
    public void testAddAmountToMyStock_WithUserId() {
        // Priprema
        String ticker = "AAPL";
        Integer amount = 10;
        Long userId = 1L;
        Double minimumPrice = 90.0;
        Ticker dummyTicker = new Ticker();
        dummyTicker.setCurrencyName("USD");

        when(myStockRepository.findByTickerAndUserId(ticker, userId)).thenReturn(null);
        when(tickerRepository.findByTicker(ticker)).thenReturn(dummyTicker);

        // Poziv metode koju testiramo
        myStockService.addAmountToMyStock(ticker, amount, userId, null, minimumPrice);

        // Provera
        verify(myStockRepository, times(1)).findByTickerAndUserId(ticker, userId);
        verify(tickerRepository, times(1)).findByTicker(ticker);

        ArgumentCaptor<MyStock> myStockCaptor = ArgumentCaptor.forClass(MyStock.class);
        verify(myStockRepository, times(1)).save(myStockCaptor.capture());

        MyStock savedMyStock = myStockCaptor.getValue();
        assertEquals(ticker, savedMyStock.getTicker());
        assertEquals(userId, savedMyStock.getUserId());
        assertEquals(amount, savedMyStock.getAmount());
        assertEquals(amount, savedMyStock.getPrivateAmount());
        assertEquals(0, savedMyStock.getPublicAmount());
        assertEquals(minimumPrice, savedMyStock.getMinimumPrice());
        assertEquals("USD", savedMyStock.getCurrencyMark());

        //verify(eventPublisher, times(1)).publishEvent(savedMyStock);
    }

    @Test
    public void testAddAmountToMyStock_WithCompanyId() {
        // Priprema
        String ticker = "AAPL";
        Integer amount = 10;
        Long companyId = 1L;
        Double minimumPrice = 90.0;
        Ticker dummyTicker = new Ticker();
        dummyTicker.setCurrencyName("USD");

        when(myStockRepository.findByTickerAndCompanyId(ticker, companyId)).thenReturn(null);
        when(tickerRepository.findByTicker(ticker)).thenReturn(dummyTicker);

        // Poziv metode koju testiramo
        myStockService.addAmountToMyStock(ticker, amount, null, companyId, minimumPrice);

        // Provera
        verify(myStockRepository, times(1)).findByTickerAndCompanyId(ticker, companyId);
        verify(tickerRepository, times(1)).findByTicker(ticker);

        ArgumentCaptor<MyStock> myStockCaptor = ArgumentCaptor.forClass(MyStock.class);
        verify(myStockRepository, times(1)).save(myStockCaptor.capture());

        MyStock savedMyStock = myStockCaptor.getValue();
        assertEquals(ticker, savedMyStock.getTicker());
        assertEquals(companyId, savedMyStock.getCompanyId());
        assertEquals(amount, savedMyStock.getAmount());
        assertEquals(amount, savedMyStock.getPrivateAmount());
        assertEquals(0, savedMyStock.getPublicAmount());
        assertEquals(minimumPrice, savedMyStock.getMinimumPrice());
        assertEquals("USD", savedMyStock.getCurrencyMark());

        //verify(eventPublisher, times(1)).publishEvent(savedMyStock);
    }
}