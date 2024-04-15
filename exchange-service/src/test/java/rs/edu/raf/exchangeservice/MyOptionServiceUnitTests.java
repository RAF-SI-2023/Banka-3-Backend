package rs.edu.raf.exchangeservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrderSell;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.OptionOrderSellRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyOptionService;


import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyOptionServiceUnitTests {

    @Mock
    private MyOptionRepository myOptionRepository;

    @Mock
    private OptionOrderSellRepository optionOrderSellRepository;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private MyOptionService myOptionService;

    @BeforeEach
    public void setUp() {
        myOptionService.ordersToSell = new CopyOnWriteArrayList<>();
    }

    @Test
    public void testAddAmountToMyOptionsOptionFound() {
        // Arrange
        Option foundOption = new Option();
        foundOption.setPrice(200.0);
        foundOption.setContractSymbol("OPT123");
        when(optionRepository.findByContractSymbol("OPT123")).thenReturn(Optional.of(foundOption));

        // Act
        myOptionService.addAmountToMyOptions("OPT123", 1, "TICK123");

        // Assert
        verify(myOptionRepository).save(argThat(myOption ->
                myOption.getAmount() == 1 &&
                        myOption.getBoughtPrice() == 200.0 &&
                        myOption.getContractSymbol().equals("OPT123") &&
                        myOption.getTicker().equals("TICK123")
        ));
    }

    @Test
    public void testAddAmountToMyOptionsOptionNotFound() {
        // Arrange
        when(optionRepository.findByContractSymbol("OPT123")).thenReturn(Optional.empty());

        // Act
        myOptionService.addAmountToMyOptions("OPT123", 1, "TICK123");

        // Assert
        verify(myOptionRepository, never()).save(any(MyOption.class));
    }

    @Test
    public void testAddAmountToMyOptionsNullContractSymbol() {
        // Act
        myOptionService.addAmountToMyOptions(null, 1, "TICK123");

        // Assert
        verify(optionRepository, never()).findByContractSymbol(anyString());
        verify(myOptionRepository, never()).save(any(MyOption.class));
    }

    @Test
    public void testSellOptionMarketOrder() {
        // Arrange
        SellOptionDto dto = new SellOptionDto();
        dto.setTicker("XYZ");
        dto.setEmployeeId(1L);
        dto.setPrice(200.0);
        dto.setMargine(true);
        dto.setStopValue(0.0);
        dto.setLimitValue(0.0);

        // Act
        String result = myOptionService.sellOption(dto);

        // Assert
        assertEquals("UBACENO U ORDER", result);
        verify(optionOrderSellRepository).save(argThat(order ->
                order.getType().equals("MARKET") &&
                        order.getPrice() == 200.0 &&
                        order.getTicker().equals("XYZ") &&
                        order.getEmployeeId().equals(1L) &&
                        order.isMargin() &&
                        order.getLimitValue() == 0.0 &&
                        order.getStopValue() == 0.0
        ));
    }

    @Test
    public void testSellOptionStopOrder() {
        // Arrange
        SellOptionDto dto = new SellOptionDto();
        dto.setTicker("XYZ");
        dto.setEmployeeId(2L);
        dto.setPrice(300.0);
        dto.setMargine(false);
        dto.setStopValue(310.0);
        dto.setLimitValue(0.0);

        // Act
        String result = myOptionService.sellOption(dto);

        // Assert
        assertEquals("UBACENO U ORDER", result);
        verify(optionOrderSellRepository).save(argThat(order ->
                order.getType().equals("STOP") &&
                        order.getPrice() == 300.0 &&
                        order.getTicker().equals("XYZ") &&
                        order.getEmployeeId().equals(2L) &&
                        !order.isMargin() &&
                        order.getLimitValue() == 0.0 &&
                        order.getStopValue() == 310.0
        ));
    }

    @Test
    public void testSellOptionLimitOrder() {
        // Arrange
        SellOptionDto dto = new SellOptionDto();
        dto.setTicker("XYZ");
        dto.setEmployeeId(3L);
        dto.setPrice(150.0);
        dto.setMargine(true);
        dto.setStopValue(0.0);
        dto.setLimitValue(145.0);

        // Act
        String result = myOptionService.sellOption(dto);

        // Assert
        assertEquals("UBACENO U ORDER", result);
        verify(optionOrderSellRepository).save(argThat(order ->
                order.getType().equals("LIMIT") &&
                        order.getPrice() == 150.0 &&
                        order.getTicker().equals("XYZ") &&
                        order.getEmployeeId().equals(3L) &&
                        order.isMargin() &&
                        order.getLimitValue() == 145.0 &&
                        order.getStopValue() == 0.0
        ));
    }

    @Test
    public void testSellOptionStopLimitOrder() {
        // Arrange
        SellOptionDto dto = new SellOptionDto();
        dto.setTicker("XYZ");
        dto.setEmployeeId(4L);
        dto.setPrice(100.0);
        dto.setMargine(false);
        dto.setStopValue(105.0);
        dto.setLimitValue(110.0);

        // Act
        String result = myOptionService.sellOption(dto);

        // Assert
        assertEquals("UBACENO U ORDER", result);
        verify(optionOrderSellRepository).save(argThat(order ->
                order.getType().equals("STOP-LIMIT") &&
                        order.getPrice() == 100.0 &&
                        order.getTicker().equals("XYZ") &&
                        order.getEmployeeId().equals(4L) &&
                        !order.isMargin() &&
                        order.getLimitValue() == 110.0 &&
                        order.getStopValue() == 105.0
        ));
    }

    @Test
    public void testExecuteTaskWithEmptyList() {
        // Ensure the list is empty
        assertTrue(myOptionService.ordersToSell.isEmpty());

        // Act
        myOptionService.executeTask();

        // Assert
        assertTrue(myOptionService.ordersToSell.isEmpty(), "List should remain empty");
    }

    @Test
    public void testExecuteTaskWithMarketOrder() {
        OptionOrderSell marketOrder = new OptionOrderSell();
        marketOrder.setType("MARKET");
        marketOrder.setPrice(100.0);
        marketOrder.setStatus("PROCESSING");

        myOptionService.ordersToSell.add(marketOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertTrue(myOptionService.ordersToSell.isEmpty(), "Market order should be removed after processing");
        assertEquals("FINISHED", marketOrder.getStatus(), "Market order status should be 'FINISHED'");
        verify(optionOrderSellRepository).save(marketOrder);
    }

    @Test
    public void testExecuteTaskWithLimitOrderSuccess() {
        OptionOrderSell limitOrder = new OptionOrderSell();
        limitOrder.setType("LIMIT");
        limitOrder.setLimitValue(150.0);
        limitOrder.setPrice(155.0);
        limitOrder.setStatus("PROCESSING");

        myOptionService.ordersToSell.add(limitOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertTrue(myOptionService.ordersToSell.isEmpty(), "Limit order should be removed after processing");
        assertEquals("FINISHED", limitOrder.getStatus(), "Limit order status should be 'FINISHED'");
        verify(optionOrderSellRepository).save(limitOrder);
    }

    @Test
    public void testExecuteTaskWithLimitOrderFailure() {
        OptionOrderSell limitOrder = new OptionOrderSell();
        limitOrder.setType("LIMIT");
        limitOrder.setLimitValue(150.0);
        limitOrder.setPrice(145.0);

        myOptionService.ordersToSell.add(limitOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertTrue(myOptionService.ordersToSell.isEmpty(), "Limit order should be removed even after processing failed");
        assertEquals("FAILED", limitOrder.getStatus(), "Limit order status should be 'FAILED'");
        verify(optionOrderSellRepository).save(limitOrder);
    }

    @Test
    public void testExecuteTaskWithStopOrderTransitionToMarket() {
        OptionOrderSell stopOrder = new OptionOrderSell();
        stopOrder.setType("STOP");
        stopOrder.setStopValue(105.0);
        stopOrder.setPrice(100.0);
        stopOrder.setStatus("PROCESSING");

        myOptionService.ordersToSell.add(stopOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertEquals("MARKET", stopOrder.getType(), "Stop order should transition to 'MARKET'");
        assertEquals("PROCESSING", stopOrder.getStatus(), "Status should remain 'PROCESSING' after type change");
        assertTrue(myOptionService.ordersToSell.contains(stopOrder), "Order should still be in the list after conversion");
        verify(optionOrderSellRepository).save(stopOrder);
    }

    @Test
    public void testExecuteTaskWithStopOrderNoTransition() {
        OptionOrderSell stopOrder = new OptionOrderSell();
        stopOrder.setType("STOP");
        stopOrder.setStopValue(100.0);
        stopOrder.setPrice(105.0);

        myOptionService.ordersToSell.add(stopOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertEquals("STOP", stopOrder.getType(), "Stop order should remain as 'STOP'");
        assertTrue(myOptionService.ordersToSell.contains(stopOrder), "Order should still be in the list without conversion");
        verify(optionOrderSellRepository).save(stopOrder);
    }

    @Test
    public void testExecuteTaskWithStopLimitOrderTransitionToLimit() {
        OptionOrderSell stopLimitOrder = new OptionOrderSell();
        stopLimitOrder.setType("STOP-LIMIT");
        stopLimitOrder.setStopValue(110.0);
        stopLimitOrder.setPrice(105.0);
        stopLimitOrder.setStatus("PROCESSING");

        myOptionService.ordersToSell.add(stopLimitOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertEquals("LIMIT", stopLimitOrder.getType(), "Stop-Limit order should transition to 'LIMIT'");
        assertEquals("PROCESSING", stopLimitOrder.getStatus(), "Status should remain 'PROCESSING' after type change");
        assertTrue(myOptionService.ordersToSell.contains(stopLimitOrder), "Order should still be in the list after conversion");
        verify(optionOrderSellRepository).save(stopLimitOrder);
    }

    @Test
    public void testExecuteTaskWithStopLimitOrderNoTransition() {
        OptionOrderSell stopLimitOrder = new OptionOrderSell();
        stopLimitOrder.setType("STOP-LIMIT");
        stopLimitOrder.setStopValue(100.0);
        stopLimitOrder.setPrice(105.0);
        stopLimitOrder.setStatus("PROCESSING");

        myOptionService.ordersToSell.add(stopLimitOrder);

        // Act
        myOptionService.executeTask();

        // Assert
        assertEquals("STOP-LIMIT", stopLimitOrder.getType(), "Stop-Limit order should remain as 'STOP-LIMIT'");
        assertTrue(myOptionService.ordersToSell.contains(stopLimitOrder), "Order should still be in the list without conversion");
        verify(optionOrderSellRepository).save(stopLimitOrder);
    }
}
