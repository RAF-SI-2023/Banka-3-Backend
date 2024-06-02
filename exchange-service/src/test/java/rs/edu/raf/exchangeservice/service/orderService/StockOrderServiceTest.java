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
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockUserOTCDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
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
    @Test
    public void testBuyUserStockOtc() {
        // Postavite scenarij za test
        BuyStockUserOTCDto buyStockUserOTCDto = new BuyStockUserOTCDto();
        buyStockUserOTCDto.setUserBuyerId(1L);
        buyStockUserOTCDto.setUserSellerId(2L);
        buyStockUserOTCDto.setTicker("AAPL");
        buyStockUserOTCDto.setPrice(BigDecimal.valueOf(100));
        buyStockUserOTCDto.setAmount(10);

        // Pozovite metodu koju testirate
        boolean result = stockOrderService.buyUserStockOtc(buyStockUserOTCDto);

        // Proverite rezultat
        assertTrue(result);

        // Proverite da li je metoda save pozvana na repository-ju
        verify(contractRepository, times(1)).save(any(Contract.class));
    }
    @Test
    public void testApproveStockOrder() {
        // Postavite scenarij za test
        Long orderId = 1L;
        boolean approved = true;

        StockOrder stockOrder = createDummyStockOrder(); // Kreirajte odgovarajuću dummy narudžbinu

        when(stockOrderRepository.findByStockOrderId(orderId)).thenReturn(stockOrder);
        when(stockOrderRepository.save(stockOrder)).thenReturn(stockOrder);

        // Pozovite metodu koju testirate
        StockOrder result = stockOrderService.approveStockOrder(orderId, approved);

        // Proverite rezultat
        assertNotNull(result); // Proverite da li je vraćena narudžbina
        assertEquals(OrderStatus.PROCESSING, result.getStatus()); // Proverite da li je status promenjen ako je narudžbina odobrena

        // Proverite da li je narudžbina sačuvana u bazi podataka
        verify(stockOrderRepository, times(1)).save(stockOrder);
    }
    @Test
    public void testBuyCompanyStockOtc() {
        // Postavite scenarij za test
        BuyStockCompanyDto buyStockCompanyDto = new BuyStockCompanyDto();
        buyStockCompanyDto.setBuyerId(1L);
        buyStockCompanyDto.setSellerId(2L);
        buyStockCompanyDto.setTicker("AAPL");
        buyStockCompanyDto.setPrice(BigDecimal.valueOf(100));
        buyStockCompanyDto.setAmount(10);

        // Pozovite metodu koju testirate
        boolean result = stockOrderService.buyCompanyStockOtc(buyStockCompanyDto);

        // Proverite rezultat
        assertTrue(result);

        // Proverite da li je metoda save pozvana na repository-ju
        verify(contractRepository, times(1)).save(any(Contract.class));
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
    private StockOrder createDummyStockOrder() {
        StockOrder stockOrder = new StockOrder();
        stockOrder.setStockOrderId(1L);
        stockOrder.setUserId(1L);
        stockOrder.setCompanyId(1L);
        stockOrder.setEmployeeId(1L);
        stockOrder.setTicker("AAPL");
        stockOrder.setStatus(OrderStatus.WAITING);
        stockOrder.setType(OrderType.MARKET);
        stockOrder.setLimitValue(100.0);
        stockOrder.setStopValue(90.0);
        stockOrder.setAmount(100);
        stockOrder.setAmountLeft(100);
        stockOrder.setAon(true);
        stockOrder.setMargin(true);
        stockOrder.setCurrencyMark("USD");

        return stockOrder;
    }

}