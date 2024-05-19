package rs.edu.raf.exchangeservice.service.orderService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.CompanyAccountDto;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.StockRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockOrderServiceTest {

    @Mock
    StockOrderRepository stockOrderRepository;
    @Mock
    StockRepository stockRepository;
    @Mock
    ActuaryRepository actuaryRepository;
    @Mock
    MyStockService myStockService;
    @Mock
    BankServiceClient bankServiceClient;
    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    StockOrderService stockOrderService;


    @Test
    public void testBuyStock_ActuaryOrderRequestTrue() {
        // Simulacija podataka
        BuySellStockDto buySellStockDto = new BuySellStockDto();
        buySellStockDto.setEmployeeId(1l);
        buySellStockDto.setAmount(10);
        buySellStockDto.setStopValue(1.0);
        buySellStockDto.setAon(true);
        buySellStockDto.setMargin(true);
        // Postavite ostale podatke po potrebi

        Actuary mockActuary = new Actuary();
        mockActuary.setOrderRequest(true);
        when(actuaryRepository.findByEmployeeId(1l)).thenReturn(mockActuary);

        // Pozivanje metode koju testiramo
        StockOrderDto result = stockOrderService.buyStock(buySellStockDto);

        // Provjera očekivanog rezultata
        assertEquals(OrderStatus.WAITING.toString(), result.getStatus());
    }

    @Test
    public void testBuyStock_ActuaryOrderRequestTrue1() {
        // Simulacija podataka
        BuySellStockDto buySellStockDto = new BuySellStockDto();
        buySellStockDto.setEmployeeId(1l);
        buySellStockDto.setAmount(10);
        buySellStockDto.setLimitValue(1.0);
        buySellStockDto.setAon(true);
        buySellStockDto.setMargin(true);
        // Postavite ostale podatke po potrebi

        Actuary mockActuary = new Actuary();
        mockActuary.setOrderRequest(true);
        when(actuaryRepository.findByEmployeeId(1l)).thenReturn(mockActuary);

        // Pozivanje metode koju testiramo
        StockOrderDto result = stockOrderService.buyStock(buySellStockDto);

        // Provjera očekivanog rezultata
        assertEquals(OrderStatus.WAITING.toString(), result.getStatus());
    }

    @Test
    public void testBuyStock_ActuaryOrderRequestTrue2() {
        // Simulacija podataka
        BuySellStockDto buySellStockDto = new BuySellStockDto();
        buySellStockDto.setEmployeeId(1l);
        buySellStockDto.setAmount(10);
        buySellStockDto.setLimitValue(1.0);
        buySellStockDto.setStopValue(1.0);
        buySellStockDto.setAon(true);
        buySellStockDto.setMargin(true);
        // Postavite ostale podatke po potrebi

        Actuary mockActuary = new Actuary();
        mockActuary.setOrderRequest(true);
        when(actuaryRepository.findByEmployeeId(1l)).thenReturn(mockActuary);

        // Pozivanje metode koju testiramo
        StockOrderDto result = stockOrderService.buyStock(buySellStockDto);

        // Provjera očekivanog rezultata
        assertEquals(OrderStatus.WAITING.toString(), result.getStatus());
    }

    @Test
    public void testBuyStock_ActuaryOrderRequestFalse() {
        // Simulacija podataka
        BuySellStockDto buySellStockDto = new BuySellStockDto();
        buySellStockDto.setEmployeeId(1l);
        buySellStockDto.setAmount(10);
        buySellStockDto.setAon(true);
        buySellStockDto.setMargin(true);
        // Postavite ostale podatke po potrebi

        Actuary mockActuary = new Actuary();
        mockActuary.setOrderRequest(false);
        when(actuaryRepository.findByEmployeeId(1l)).thenReturn(mockActuary);

        // Pozivanje metode koju testiramo
        StockOrderDto result = stockOrderService.buyStock(buySellStockDto);

        // Provjera očekivanog rezultata
        assertEquals(OrderStatus.PROCESSING.toString(), result.getStatus());
    }
//    @Test
//    void executeTaskTest() {
//        // Simulacija podataka
//        StockOrder stockOrder = new StockOrder();
//        stockOrder.setEmployeeId(1l);
//        stockOrder.setAmount(10);
//        stockOrder.setAmountLeft(10);
//        stockOrder.setAon(true);
//        stockOrder.setMargin(true);
//        stockOrder.setStatus(OrderStatus.WAITING);
//        stockOrder.setType(OrderType.MARKET);
//        stockOrder.setStopValue(0.0);
//        stockOrder.setLimitValue(0.0);
//
//        Stock stock = new Stock();
//        stock.setTicker("AAPL");
//        stock.setPrice(100.0);
//
//        Actuary actuary = new Actuary();
//        actuary.setActuaryId(1l);
//        actuary.setEmployeeId(1l);
//        actuary.setLimitValue(2.0);
//        actuary.setLimitUsed(0.0);
//        actuary.setOrderRequest(true);
//        actuary.setRole("ROLE_SUPERVISOR");
//        actuary.setEmail("email");
//
//        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
//        given(stockRepository.findByTicker(any())).willReturn(Optional.of(stock));
//        stockOrderService.ordersToBuy.add(stockOrder);
//        stockOrderService.executeTask();
//
//        verify(actuaryRepository, atLeastOnce()).findByEmployeeId(any());
//        verify(actuaryRepository, atLeastOnce()).save(actuary);
//        verify(stockOrderRepository, times(1)).save(stockOrder);
//        stockOrderService.ordersToBuy.remove(stockOrder);
//    }

    @Test
    void testExecuteTask() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setType(OrderType.MARKET);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
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
        stockOrder.setType(OrderType.LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        //assertEquals(OrderType.FAILED, stockOrder.getStatus());
    }

    @Test
    void testExecuteTaskWithStopOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(OrderType.STOP);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(OrderType.STOP, stockOrder.getType());
    }

    @Test
    void testExecuteTaskWithStopLimitOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(OrderType.STOP_LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(OrderType.STOP_LIMIT, stockOrder.getType());
    }

    @Test
    void testExecuteTaskWithLimitOrderAndPriceLessThanLimit() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setLimitValue(110.0); // Set a limit value greater than the current price
        stockOrder.setType(OrderType.LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        //when(bankServiceClient.stockBuyTransaction(any()).getStatusCode());

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
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
        stockOrder.setType(OrderType.MARKET);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        //when(bankServiceClient.stockBuyTransaction(any()).getStatusCode());

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
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
        stockOrder.setType(OrderType.STOP);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(OrderType.STOP, stockOrder.getType());
    }

    @Test
    void testExecuteTaskWithStopLimitOrderAndPriceLessThanStopValue() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setTicker("AAPL");
        stockOrder.setAmount(10);
        stockOrder.setAmountLeft(10);
        stockOrder.setStopValue(110.0); // Set a stop value greater than the current price
        stockOrder.setType(OrderType.STOP_LIMIT);
        stockOrderService.ordersToBuy.add(stockOrder);

        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setAsk(100.0);
        when(stockRepository.findByTicker(anyString())).thenReturn(Optional.of(stock));

        Actuary actuary = new Actuary();
        actuary.setActuaryId(1l);
        actuary.setEmployeeId(1l);
        actuary.setLimitValue(2.0);
        actuary.setLimitUsed(0.0);
        actuary.setOrderRequest(true);
        actuary.setRole("ROLE_SUPERVISOR");
        actuary.setEmail("email");

        given(actuaryRepository.findByEmployeeId(any())).willReturn(actuary);
        stockOrderService.executeTask();

        verify(stockOrderRepository, times(1)).save(any(StockOrder.class));
        assertEquals(OrderType.STOP_LIMIT, stockOrder.getType());
    }

    @Test
    public void testFindAll() {

        List<StockOrder> stockOrders = createDummyStockOrders();

        given(stockOrderRepository.findAll()).willReturn(stockOrders);

        List<StockOrder> stockOrderList = stockOrderService.findAll();
        for (StockOrder stockOrder : stockOrderList) {
            boolean found = false;
            for (StockOrder stockOrder1 : stockOrders) {
                if (stockOrder.getStockOrderId().equals(stockOrder1.getStockOrderId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                fail("Stock order not found!");
            }
        }
    }

    @Test
    public void testFindByEmployeeId() {

        Long employeeId = 0L;
        List<StockOrder> stockOrders = createDummyStockOrders();
        for (StockOrder stockOrder : stockOrders)
            stockOrder.setEmployeeId(employeeId);

        given(stockOrderRepository.findByEmployeeId(employeeId)).willReturn(stockOrders);

        List<StockOrder> stockOrderList = stockOrderService.findAllByEmployee(employeeId);
        for (StockOrder stockOrder : stockOrderList) {
            boolean found = false;
            for (StockOrder stockOrder1 : stockOrders) {
                if (stockOrder.getStockOrderId().equals(stockOrder1.getStockOrderId()) &&
                        stockOrder.getEmployeeId().equals(stockOrder1.getEmployeeId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                fail("Stock order not found!");
            }
        }
    }

    @Test
    public void testGetAllOrdersToApprove() {

        List<StockOrder> stockOrders = createDummyStockOrders();
        for (StockOrder stockOrder : stockOrders)
            stockOrder.setStatus(OrderStatus.WAITING);

        given(stockOrderRepository.findStockOrderByStatus(OrderStatus.WAITING)).willReturn(stockOrders);

        List<StockOrder> stockOrderList = stockOrderService.getAllOrdersToApprove();
        for (StockOrder stockOrder : stockOrderList) {
            boolean found = false;
            for (StockOrder stockOrder1 : stockOrders) {
                if (stockOrder.getStockOrderId().equals(stockOrder1.getStockOrderId()) &&
                        stockOrder.getStatus().equals(stockOrder1.getStatus())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                fail("Stock order not found!");
            }
        }
    }

    @Test
    public void testApproveStocksOrder() {
        Long stockOrderId = 0L;

        StockOrder stockOrder = createDummyStockOrder();
        stockOrder.setStockOrderId(stockOrderId);

        given(stockOrderRepository.findByStockOrderId(stockOrderId)).willReturn(stockOrder);
        given(stockOrderRepository.save(stockOrder)).willReturn(stockOrder);

        stockOrder = stockOrderService.approveStockOrder(stockOrderId, true);
        assertEquals(stockOrder.getStatus(), OrderStatus.PROCESSING);

        stockOrder = stockOrderService.approveStockOrder(stockOrderId, false);
        assertEquals(stockOrder.getStatus(), OrderStatus.REJECTED);
    }

    @Test
    public void testBuyStock() {
        Long employeeId = 0L;

        BuySellStockDto buySellStockDto = createDummyBuySellStockDto();
        buySellStockDto.setEmployeeId(employeeId);

        Actuary actuary = createDummyActuary();
        actuary.setEmployeeId(employeeId);
        actuary.setOrderRequest(true);

        StockOrder stockOrder = new StockOrder();
        stockOrder.setEmployeeId(buySellStockDto.getEmployeeId());
        stockOrder.setTicker(buySellStockDto.getTicker());
        stockOrder.setAmount(buySellStockDto.getAmount());
        stockOrder.setAmountLeft(buySellStockDto.getAmount());
        stockOrder.setAon(buySellStockDto.isAon());
        stockOrder.setMargin(buySellStockDto.isMargin());

        given(actuaryRepository.findByEmployeeId(employeeId)).willReturn(actuary);
        given(stockOrderRepository.save(any())).willReturn(stockOrder);

        StockOrderDto stockOrderDto = stockOrderService.buyStock(buySellStockDto);
        assertEquals(stockOrderDto.getEmployeeId(), stockOrder.getEmployeeId());
        assertEquals(stockOrderDto.getStatus(), OrderStatus.WAITING.toString());

        buySellStockDto.setStopValue(0.0);
        buySellStockDto.setLimitValue(0.0);
        stockOrderDto = stockOrderService.buyStock(buySellStockDto);
        assertEquals(stockOrderDto.getType(), OrderType.MARKET.toString());

        buySellStockDto.setStopValue(100.0);
        buySellStockDto.setLimitValue(0.0);
        stockOrderDto = stockOrderService.buyStock(buySellStockDto);
        assertEquals(stockOrderDto.getType(), OrderType.STOP.toString());

        buySellStockDto.setStopValue(0.0);
        buySellStockDto.setLimitValue(100.0);
        stockOrderDto = stockOrderService.buyStock(buySellStockDto);
        assertEquals(stockOrderDto.getType(), OrderType.LIMIT.toString());

        buySellStockDto.setStopValue(100.0);
        buySellStockDto.setLimitValue(100.0);
        stockOrderDto = stockOrderService.buyStock(buySellStockDto);
        assertEquals(stockOrderDto.getType(), OrderType.STOP_LIMIT.toString());

    }

    @Test
    public void testBuyCompanyStockOtc() {

        BuyStockCompanyDto buyStockCompanyDto = new BuyStockCompanyDto();
        buyStockCompanyDto.setBuyerId(1L);
        buyStockCompanyDto.setSellerId(2L);
        buyStockCompanyDto.setTicker("AAPL");
        buyStockCompanyDto.setPrice(BigDecimal.valueOf(100));
        buyStockCompanyDto.setAmount(10);

        CompanyAccountDto companyAccountDto = new CompanyAccountDto();
        companyAccountDto.setAvailableBalance(BigDecimal.valueOf(2000));

        ResponseEntity responseEntity = ResponseEntity.ok(companyAccountDto);

        given(bankServiceClient.getByCompanyId(any(Long.class))).willReturn(responseEntity);

        boolean result = stockOrderService.buyCompanyStockOtc(buyStockCompanyDto);

        assertTrue(result);
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    public StockOrder createDummyStockOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setStockOrderId(1L);

        return stockOrder;
    }

    public List<StockOrder> createDummyStockOrders() {
        StockOrder stockOrder1 = new StockOrder();
        stockOrder1.setStockOrderId(1L);

        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setStockOrderId(2L);

        StockOrder stockOrder3 = new StockOrder();
        stockOrder3.setStockOrderId(3L);

        return List.of(stockOrder1, stockOrder2, stockOrder3);
    }

    public BuySellStockDto createDummyBuySellStockDto() {
        BuySellStockDto buySellStockDto = new BuySellStockDto();
        buySellStockDto.setEmployeeId(0L);
        buySellStockDto.setTicker("ticker");
        buySellStockDto.setAmount(10);
        buySellStockDto.setLimitValue(10.0);
        buySellStockDto.setStopValue(10.0);
        buySellStockDto.setAon(false);
        buySellStockDto.setMargin(false);

        return buySellStockDto;
    }

    public Actuary createDummyActuary() {
        Actuary actuary = new Actuary();
        actuary.setActuaryId(0L);
        actuary.setEmployeeId(0L);

        return actuary;
    }
}