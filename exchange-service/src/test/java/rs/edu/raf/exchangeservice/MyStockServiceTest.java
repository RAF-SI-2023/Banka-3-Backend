package rs.edu.raf.exchangeservice;

import org.apache.catalina.core.ApplicationPushBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class MyStockServiceTest {
    @Mock
    private MyStockRepository myStockRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockOrderSellRepository stockOrderSellRepository;

    @Mock
    private BankServiceClient bankServiceClient;

    @Mock
    private TickerRepository tickerRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MyStockService myStockService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadData_WhenTickersExist_ShouldCreateMyStocks() {
        // Given
        Ticker ticker1 = new Ticker();
        ticker1.setTicker("AAPL");
        ticker1.setCurrencyName("USD");

        Ticker ticker2 = new Ticker();
        ticker2.setTicker("GOOG");
        ticker2.setCurrencyName("USD");

        List<Ticker> tickersList = Arrays.asList(ticker1, ticker2);
        when(tickerRepository.findAll()).thenReturn(tickersList);

        // When
        myStockService.loadData();

        // Then
        verify(myStockRepository, times(2)).save(any(MyStock.class));
    }

    @Test
    public void loadData_WhenNoTickersExist_ShouldNotCreateMyStocks() {
        // Given
        when(tickerRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        myStockService.loadData();

        // Then
        verify(myStockRepository, never()).save(any(MyStock.class));
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
    public void sellStock_MarketOrder() {
        // Setup
        BuySellStockDto sellStockDto = new BuySellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(0.0);
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setAon(false);
        sellStockDto.setMargin(false);

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(15);

        StockOrderSell expectedOrder = new StockOrderSell();
        expectedOrder.setTicker("AAPL");
        expectedOrder.setAmount(10);
        expectedOrder.setStatus(OrderStatus.PROCESSING);
        expectedOrder.setType(OrderType.MARKET);
        expectedOrder.setLimitValue(0.0);
        expectedOrder.setStopValue(0.0);
        expectedOrder.setAmountLeft(10); // Adjusted

        when(myStockRepository.findByTicker("AAPL")).thenReturn(myStock);
        when(stockOrderSellRepository.save(any())).thenReturn(expectedOrder);

        // Execution
        myStockService.sellStock(sellStockDto);

        // Verification
        verify(stockOrderSellRepository, times(1)).save(expectedOrder);
        assertEquals(OrderStatus.PROCESSING, expectedOrder.getStatus());
        assertEquals(15, myStock.getAmount());
    }
    @Test
    public void sellStock_StopOrder() {
        // Setup
        BuySellStockDto sellStockDto = new BuySellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(140.0); // Stop price
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setAon(false);
        sellStockDto.setMargin(false);

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(15);

        StockOrderSell expectedOrder = new StockOrderSell();
        expectedOrder.setTicker("AAPL");
        expectedOrder.setAmount(10);
        expectedOrder.setStatus(OrderStatus.PROCESSING);
        expectedOrder.setType(OrderType.STOP);
        expectedOrder.setLimitValue(0.0);
        expectedOrder.setStopValue(140.0);
        expectedOrder.setAmountLeft(10); // Adjusted

        when(myStockRepository.findByTicker("AAPL")).thenReturn(myStock);
        when(stockOrderSellRepository.save(any())).thenReturn(expectedOrder);

        // Execution
        myStockService.sellStock(sellStockDto);

        // Verification
        verify(stockOrderSellRepository, times(1)).save(expectedOrder);
        assertEquals(OrderStatus.PROCESSING, expectedOrder.getStatus());
        assertEquals(15, myStock.getAmount());
    }
    @Test
    public void sellStock_limitOrder() {
        // Setup
        BuySellStockDto sellStockDto = new BuySellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(0.0);
        sellStockDto.setLimitValue(150.0); // Limit order price
        sellStockDto.setAon(false);
        sellStockDto.setMargin(false);

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(15);

        StockOrderSell expectedOrder = new StockOrderSell();
        expectedOrder.setTicker("AAPL");
        expectedOrder.setAmount(10);
        expectedOrder.setStatus(OrderStatus.PROCESSING);
        expectedOrder.setType(OrderType.LIMIT);
        expectedOrder.setLimitValue(150.0);
        expectedOrder.setStopValue(0.0);
        expectedOrder.setAmountLeft(10); // Adjusted

        when(myStockRepository.findByTicker("AAPL")).thenReturn(myStock);
        when(stockOrderSellRepository.save(any())).thenReturn(expectedOrder);

        // Execution
        myStockService.sellStock(sellStockDto);

        // Verification
        verify(stockOrderSellRepository, times(1)).save(expectedOrder);
        assertEquals(OrderStatus.PROCESSING, expectedOrder.getStatus());
        assertEquals(15, myStock.getAmount());
    }
    @Test
    public void sellStock_stop_limitOrder() {
        // Setup
        BuySellStockDto sellStockDto = new BuySellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(140.0); // Stop price
        sellStockDto.setLimitValue(150.0); // Limit price
        sellStockDto.setAon(false);
        sellStockDto.setMargin(false);

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(15);

        StockOrderSell expectedOrder = new StockOrderSell();
        expectedOrder.setTicker("AAPL");
        expectedOrder.setAmount(10);
        expectedOrder.setStatus(OrderStatus.PROCESSING);
        expectedOrder.setType(OrderType.STOP_LIMIT);
        expectedOrder.setLimitValue(150.0);
        expectedOrder.setStopValue(140.0);
        expectedOrder.setAmountLeft(10); // Adjusted

        when(myStockRepository.findByTicker("AAPL")).thenReturn(myStock);
        when(stockOrderSellRepository.save(any())).thenReturn(expectedOrder);

        // Execution
        myStockService.sellStock(sellStockDto);

        // Verification
        verify(stockOrderSellRepository, times(1)).save(expectedOrder);
        assertEquals(OrderStatus.PROCESSING, expectedOrder.getStatus());
        assertEquals(15, myStock.getAmount());
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
    public void addAmountToMyStock() {
        String ticker = "AAPL";
        int amount = 10;
        MyStock myStock = new MyStock();
        myStock.setTicker(ticker);
        myStock.setAmount(5);
        when(myStockRepository.findByTicker(ticker)).thenReturn(myStock);

        myStockService.addAmountToMyStock(ticker, amount);

        assertEquals(15, myStock.getAmount());
        verify(myStockRepository, times(1)).save(myStock);
    }

}
