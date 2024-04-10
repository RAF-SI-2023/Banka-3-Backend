package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.exchangeservice.domain.dto.SellStockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderSellRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MyStockServiceTest {

    @Mock
    private MyStockRepository myStockRepository;

    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private StockOrderSellRepository stockOrderSellRepository;
    @Mock
    private StockRepository stockRepository;



    @InjectMocks
    private MyStockService myStockService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadData() {
        // Arrange
        Ticker ticker1 = new Ticker();
        ticker1.setTicker("AAPL");
        Ticker ticker2 = new Ticker();
        ticker2.setTicker("GOOG");
        when(tickerRepository.findAll()).thenReturn(Arrays.asList(ticker1, ticker2));

        // Act
        myStockService.loadData();

        // Assert
        verify(tickerRepository, times(1)).findAll();
        verify(myStockRepository, times(2)).save(any(MyStock.class));
    }

    @Test
    public void testAddAmountToMyStock() {
        // Arrange
        String ticker = "AAPL";
        Integer amount = 10;
        MyStock myStock = new MyStock();
        myStock.setTicker(ticker);
        myStock.setAmount(0);
        when(myStockRepository.findByTicker(ticker)).thenReturn(myStock);

        // Act
        myStockService.addAmountToMyStock(ticker, amount);

        // Assert
        verify(myStockRepository, times(1)).findByTicker(ticker);
        verify(myStockRepository, times(1)).save(any(MyStock.class));
    }

    @Test
    public void testGetAll() {
        // Arrange
        MyStock myStock1 = new MyStock();
        myStock1.setTicker("AAPL");
        myStock1.setAmount(10);
        MyStock myStock2 = new MyStock();
        myStock2.setTicker("GOOG");
        myStock2.setAmount(20);
        when(myStockRepository.findAll()).thenReturn(Arrays.asList(myStock1, myStock2));

        // Act
        List<MyStock> result = myStockService.getAll();

        // Assert
        verify(myStockRepository, times(1)).findAll();
        assert (result.size() == 2);
    }

    @Test
    public void testSellStock() {
    // Arrange
        SellStockDto sellStockDto = new SellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(0.0);
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setAon(false);
        sellStockDto.setMargine(false);
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(sellStockDto.getTicker())).thenReturn(myStock);

        // Act
        String result = myStockService.sellStock(sellStockDto);

        // Assert
        verify(myStockRepository, times(1)).findByTicker(sellStockDto.getTicker());
        verify(stockOrderSellRepository, times(1)).save(any(StockOrderSell.class));
        assertEquals("UBACENO U ORDER", result);
    }

    @Test
    public void testSellStockWithInsufficientAmount() {
        // Arrange
        SellStockDto sellStockDto = new SellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(30); // Amount greater than available stock
        sellStockDto.setStopValue(0.0);
        sellStockDto.setLimitValue(0.0);
        sellStockDto.setAon(false);
        sellStockDto.setMargine(false);
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(sellStockDto.getTicker())).thenReturn(myStock);

        // Act
        String result = myStockService.sellStock(sellStockDto);

        // Assert
        verify(myStockRepository, times(1)).findByTicker(sellStockDto.getTicker());
        verify(stockOrderSellRepository, times(1)).save(any(StockOrderSell.class));
        assertEquals("nije dobar amount", result);
    }

    @Test
    public void testSellStockStopOrder() {
        // Arrange
        SellStockDto sellStockDto = new SellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(1.0); // Non-zero stop value
        sellStockDto.setLimitValue(0.0); // Zero limit value
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(sellStockDto.getTicker())).thenReturn(myStock);

        // Act
        String result = myStockService.sellStock(sellStockDto);

        // Assert
        verify(myStockRepository, times(1)).findByTicker(sellStockDto.getTicker());
        verify(stockOrderSellRepository, times(1)).save(argThat(order -> order.getType().equals("STOP")));
    }

    @Test
    public void testSellStockLimitOrder() {
        // Arrange
        SellStockDto sellStockDto = new SellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(0.0); // Zero stop value
        sellStockDto.setLimitValue(1.0); // Non-zero limit value
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(sellStockDto.getTicker())).thenReturn(myStock);

        // Act
        String result = myStockService.sellStock(sellStockDto);

        // Assert
        verify(myStockRepository, times(1)).findByTicker(sellStockDto.getTicker());
        verify(stockOrderSellRepository, times(1)).save(argThat(order -> order.getType().equals("LIMIT")));
    }

    @Test
    public void testSellStockStopLimitOrder() {
        // Arrange
        SellStockDto sellStockDto = new SellStockDto();
        sellStockDto.setTicker("AAPL");
        sellStockDto.setAmount(10);
        sellStockDto.setStopValue(1.0); // Non-zero stop value
        sellStockDto.setLimitValue(1.0); // Non-zero limit value
        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(sellStockDto.getTicker())).thenReturn(myStock);

        // Act
        String result = myStockService.sellStock(sellStockDto);

        // Assert
        verify(myStockRepository, times(1)).findByTicker(sellStockDto.getTicker());
        verify(stockOrderSellRepository, times(1)).save(argThat(order -> order.getType().equals("STOP-LIMIT")));
    }

    @Test
    public void testExecuteTask() {
        // Arrange
        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setTicker("AAPL");
        stockOrderSell.setAmount(10);
        stockOrderSell.setAmountLeft(10);
        stockOrderSell.setStopValue(0.0);
        stockOrderSell.setLimitValue(0.0);
        stockOrderSell.setType("MARKET");
        stockOrderSell.setAon(false);
        stockOrderSell.setMargin(false);
        myStockService.ordersToSell.add(stockOrderSell);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setBid(100.0);
        stock.setAsk(101.0);
        stock.setVolume(1000L);
        stock.setChange(1.0);
        when(stockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(Optional.of(stock));

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(myStock);

        // Act
        myStockService.executeTask();

        // Assert
        verify(stockRepository, times(1)).findByTicker(stockOrderSell.getTicker());
        verify(stockOrderSellRepository, times(1)).save(any(StockOrderSell.class));
        //assertEquals(0, myStockService.ordersToSell.size());
    }

    @Test
    public void testExecuteTaskWithLimitOrder() {
        // Arrange
        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setTicker("AAPL");
        stockOrderSell.setAmount(10);
        stockOrderSell.setAmountLeft(10);
        stockOrderSell.setLimitValue(90.0); // Set a limit value less than the current price
        stockOrderSell.setType("LIMIT");
        myStockService.ordersToSell.add(stockOrderSell);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setBid(100.0);
        when(stockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(Optional.of(stock));

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(myStock);

        // Act
        myStockService.executeTask();

        // Assert
        verify(stockRepository, times(1)).findByTicker(stockOrderSell.getTicker());
        verify(stockOrderSellRepository, times(1)).save(any(StockOrderSell.class));
        //assertEquals("FAILED", stockOrderSell.getStatus());
    }

    @Test
    public void testExecuteTaskWithStopOrder() {
        // Arrange
        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setTicker("AAPL");
        stockOrderSell.setAmount(10);
        stockOrderSell.setAmountLeft(10);
        stockOrderSell.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrderSell.setType("STOP");
        myStockService.ordersToSell.add(stockOrderSell);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setBid(100.0);
        when(stockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(Optional.of(stock));

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(myStock);

        // Act
        myStockService.executeTask();

        // Assert
        verify(stockRepository, times(1)).findByTicker(stockOrderSell.getTicker());
        verify(stockOrderSellRepository, times(1)).save(any(StockOrderSell.class));
        assertEquals("MARKET", stockOrderSell.getType());
    }

    @Test
    public void testExecuteTaskWithStopLimitOrder() {
        // Arrange
        StockOrderSell stockOrderSell = new StockOrderSell();
        stockOrderSell.setTicker("AAPL");
        stockOrderSell.setAmount(10);
        stockOrderSell.setAmountLeft(10);
        stockOrderSell.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrderSell.setType("STOP-LIMIT");
        myStockService.ordersToSell.add(stockOrderSell);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setBid(100.0);
        when(stockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(Optional.of(stock));

        MyStock myStock = new MyStock();
        myStock.setTicker("AAPL");
        myStock.setAmount(20);
        when(myStockRepository.findByTicker(stockOrderSell.getTicker())).thenReturn(myStock);

        // Act
        myStockService.executeTask();

        // Assert
        verify(stockRepository, times(1)).findByTicker(stockOrderSell.getTicker());
        verify(stockOrderSellRepository, times(1)).save(any(StockOrderSell.class));
        assertEquals("LIMIT", stockOrderSell.getType());
    }
}