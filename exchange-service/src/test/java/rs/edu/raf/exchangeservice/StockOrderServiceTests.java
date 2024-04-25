package rs.edu.raf.exchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuySellStockDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.StockOrderRepository;
import rs.edu.raf.exchangeservice.service.orderService.StockOrderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StockOrderServiceTests {

    @Mock
    private StockOrderRepository stockOrderRepository;

    @Mock
    private ActuaryRepository actuaryRepository;

    @InjectMocks
    private StockOrderService stockOrderService;

    @Test
    public void testFindAll() {

        List<StockOrder> stockOrders = createDummyStockOrders();

        given(stockOrderRepository.findAll()).willReturn(stockOrders);

        List<StockOrder> stockOrderList = stockOrderService.findAll();
        for(StockOrder stockOrder: stockOrderList) {
            boolean found = false;
            for(StockOrder stockOrder1: stockOrders) {
                if(stockOrder.getStockOrderId().equals(stockOrder1.getStockOrderId())) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                fail("Stock order not found!");
            }
        }
    }

    @Test
    public void testFindByEmployeeId() {

        Long employeeId = 0L;
        List<StockOrder> stockOrders = createDummyStockOrders();
        for(StockOrder stockOrder: stockOrders)
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
        for(StockOrder stockOrder: stockOrders)
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
