package rs.edu.raf.exchangeservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.BuyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.StockTransactionDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;
import rs.edu.raf.exchangeservice.service.orderService.StockOrderService;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StockOrderServiceTest {

    @InjectMocks
    private StockOrderService stockOrderService;

    @Mock
    private StockOrderRepository stockOrderRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ActuaryRepository actuaryRepository;

    @Mock
    private MyStockService myStockService;

    @Mock
    private BankServiceClient bankServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAll() {
        when(stockOrderRepository.findAll()).thenReturn(Arrays.asList(new StockOrder()));
        List<StockOrder> result = stockOrderService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByEmployee() {
        when(stockOrderRepository.findByEmployeeId(anyLong())).thenReturn(Arrays.asList(new StockOrder()));
        List<StockOrder> result = stockOrderService.findAllByEmployee(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllOrdersToApprove() {
        StockOrder stockOrder = new StockOrder();
        stockOrderService.ordersToApprove.add(stockOrder);
        List<StockOrder> result = stockOrderService.getAllOrdersToApprove();
        assertEquals(1, result.size());
    }

    @Test
    void testApproveStockOrder() {
        StockOrder stockOrder = new StockOrder();
        when(stockOrderRepository.findByStockOrderId(anyLong())).thenReturn(stockOrder);
        when(stockOrderRepository.save(any(StockOrder.class))).thenReturn(stockOrder);
        StockOrder result = stockOrderService.approveStockOrder(1L, true);
        assertEquals(StockOrderStatus.PROCESSING, result.getStatus());
    }

    @Test
    void testBuyStock() {
        BuyStockDto buyStockDto = new BuyStockDto();
        buyStockDto.setEmployeeId(1L);
        buyStockDto.setTicker("AAPL");
        buyStockDto.setAmount(10);
        buyStockDto.setAon(false);
        buyStockDto.setMargin(false);
        when(actuaryRepository.findByEmployeeId(anyLong())).thenReturn(new Actuary());
        when(stockOrderRepository.save(any(StockOrder.class))).thenReturn(new StockOrder());
        StockOrderDto result = stockOrderService.buyStock(buyStockDto);
        assertNotNull(result);
    }

    @Test
    void testExecuteTask() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setType(StockOrderType.MARKET);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
    }

    @Test
    void testExecuteTaskWithLimitOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setLimitValue(90.0); // Set a limit value less than the current price
        stockOrder.setType(StockOrderType.LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(StockOrderStatus.FAILED, stockOrder.getStatus());
    }

    @Test
    void testExecuteTaskWithStopOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(StockOrderType.STOP);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(StockOrderType.STOP, stockOrder.getType());
    }

    @Test
    void testExecuteTaskWithStopLimitOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(StockOrderType.STOP_LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(StockOrderType.STOP_LIMIT, stockOrder.getType());
    }

    @Test
    void testExecuteTaskWithLimitOrderAndPriceLessThanLimit() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setLimitValue(110.0); // Set a limit value greater than the current price
        stockOrder.setType(StockOrderType.LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        when(bankServiceClient.startStockTransaction(any(StockTransactionDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        //assertEquals(StockOrderStatus.FINISHED, stockOrder.getStatus());
    }

    @Test
    void testExecuteTaskWithMarketOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setType(StockOrderType.MARKET);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        when(bankServiceClient.startStockTransaction(any(StockTransactionDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        //assertEquals(StockOrderStatus.FINISHED, stockOrder.getStatus());
    }

    @Test
    void testExecuteTaskWithStopOrderAndPriceLessThanStopValue() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(StockOrderType.STOP);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(StockOrderType.STOP, stockOrder.getType());
    }

    @Test
    void testExecuteTaskWithStopLimitOrderAndPriceLessThanStopValue() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(StockOrderType.STOP_LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(StockOrderType.STOP_LIMIT, stockOrder.getType());
    }
}