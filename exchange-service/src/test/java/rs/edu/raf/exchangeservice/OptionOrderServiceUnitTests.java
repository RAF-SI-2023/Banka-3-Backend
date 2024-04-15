package rs.edu.raf.exchangeservice;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import rs.edu.raf.exchangeservice.domain.dto.BuyOptionDto;
import rs.edu.raf.exchangeservice.domain.dto.OptionOrderDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrder;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.OptionOrderRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyOptionService;
import rs.edu.raf.exchangeservice.service.orderService.OptionOrderService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@ExtendWith(MockitoExtension.class)
public class OptionOrderServiceUnitTests {

    @Mock private OptionOrderRepository optionOrderRepository;
    @Mock private OptionRepository optionRepository;
    @Mock private ActuaryRepository actuaryRepository;
    @Mock private MyOptionService myOptionService;

    @InjectMocks private OptionOrderService optionOrderService;

    private OptionOrder optionOrder;
    @BeforeEach
    public void setUp() {
        optionOrderService.ordersToApprove = new CopyOnWriteArrayList<>();
        optionOrderService.ordersToBuy = new CopyOnWriteArrayList<>();
        optionOrder = new OptionOrder();
        optionOrder.setOptionOrderId(1L);
    }

    private OptionOrder captureSavedOptionOrder() {
        ArgumentCaptor<OptionOrder> captor = ArgumentCaptor.forClass(OptionOrder.class);
        verify(optionOrderRepository).save(captor.capture());
        return captor.getValue();
    }

    @Test
    public void testFindAll() {
        when(optionOrderRepository.findAll()).thenReturn(Arrays.asList(new OptionOrder(), new OptionOrder()));
        List<OptionOrder> result = optionOrderService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(optionOrderRepository).findAll();
    }

    @Test
    public void testApproveOptionOrderApproved() {
        when(optionOrderRepository.findByOptionOrderId(1L)).thenReturn(optionOrder);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));
        optionOrderService.ordersToApprove.add(optionOrder);

        OptionOrder result = optionOrderService.approveOptionOrder(1L, true);

        assertEquals("PROCESSING", result.getStatus());
        assertTrue(optionOrderService.ordersToBuy.contains(optionOrder));
        assertFalse(optionOrderService.ordersToApprove.contains(optionOrder));
        verify(optionOrderRepository).save(optionOrder);
    }


    @Test
    public void testApproveOptionOrderRejected() {
            when(optionOrderRepository.findByOptionOrderId(1L)).thenReturn(optionOrder);
            when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));
            optionOrderService.ordersToApprove.add(optionOrder);

            OptionOrder result = optionOrderService.approveOptionOrder(1L, false);

            assertEquals("REJECTED", result.getStatus());
            assertFalse(optionOrderService.ordersToApprove.contains(optionOrder));
            assertFalse(optionOrderService.ordersToBuy.contains(optionOrder));
            verify(optionOrderRepository).save(optionOrder);
    }

    @Test
    public void testBuyOptionWithActuaryApproval() {
        // Arrange
        BuyOptionDto buyOptionDto = new BuyOptionDto();
        buyOptionDto.setEmployeeId(1L);
        buyOptionDto.setContractSymbol("OPT100");
        buyOptionDto.setPrice(100.0);
        buyOptionDto.setTicker("TICK100");
        buyOptionDto.setStopValue(0.0);
        buyOptionDto.setLimitValue(0.0);

        Actuary actuary = new Actuary(1L, 1L, "user@example.com", "actuary", 5000.0, 0.0, true);
        when(actuaryRepository.findByEmployeeId(1L)).thenReturn(actuary);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        OptionOrderDto result = optionOrderService.buyOption(buyOptionDto);

        // Capture the saved OptionOrder
        OptionOrder savedOrder = captureSavedOptionOrder();

        // Assert
        assertEquals("WAITING", result.getStatus(), "Status should be WAITING as actuary allows only request to be made");
        assertTrue(optionOrderService.ordersToApprove.contains(savedOrder), "Order should be in ordersToApprove");
        assertFalse(optionOrderService.ordersToBuy.contains(savedOrder), "Order should not be in ordersToBuy");
        verify(optionOrderRepository).save(any(OptionOrder.class));
    }

    @Test
    public void testBuyOptionWithActuaryRejection() {
        BuyOptionDto buyOptionDto = new BuyOptionDto();
        buyOptionDto.setEmployeeId(1L);
        buyOptionDto.setContractSymbol("OPT200");
        buyOptionDto.setPrice(200.0);
        buyOptionDto.setTicker("TICK200");

        Actuary actuary = new Actuary(1L, 1L, "user@example.com", "actuary", 5000.0, 0.0, false);
        when(actuaryRepository.findByEmployeeId(1L)).thenReturn(actuary);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        OptionOrderDto result = optionOrderService.buyOption(buyOptionDto);

        OptionOrder savedOrder = captureSavedOptionOrder();

        assertEquals("PROCESSING", result.getStatus());
        assertFalse(optionOrderService.ordersToApprove.contains(savedOrder));
        assertTrue(optionOrderService.ordersToBuy.contains(savedOrder));
        verify(optionOrderRepository).save(any(OptionOrder.class));
    }


    @Test
    public void testBuyOptionMarketOrder() {
        BuyOptionDto buyOptionDto = new BuyOptionDto();
        buyOptionDto.setEmployeeId(1L);
        buyOptionDto.setContractSymbol("OPT100");
        buyOptionDto.setPrice(100.0);
        buyOptionDto.setTicker("TICK100");
        buyOptionDto.setStopValue(0.0);
        buyOptionDto.setLimitValue(0.0);

        Actuary actuary = new Actuary(1L, 1L, "user@example.com", "actuary", 5000.0, 0.0, true);
        when(actuaryRepository.findByEmployeeId(1L)).thenReturn(actuary);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrderService.buyOption(buyOptionDto);

        OptionOrder capturedOrder = captureSavedOptionOrder();
        assertEquals("MARKET", capturedOrder.getType());
    }

    @Test
    public void testBuyOptionStopOrder() {
        BuyOptionDto buyOptionDto = new BuyOptionDto();
        buyOptionDto.setEmployeeId(1L);
        buyOptionDto.setContractSymbol("OPT200");
        buyOptionDto.setPrice(200.0);
        buyOptionDto.setTicker("TICK200");
        buyOptionDto.setStopValue(210.0);
        buyOptionDto.setLimitValue(0.0);

        Actuary actuary = new Actuary(1L, 1L, "user@example.com", "actuary", 5000.0, 0.0, true);
        when(actuaryRepository.findByEmployeeId(1L)).thenReturn(actuary);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrderService.buyOption(buyOptionDto);

        OptionOrder capturedOrder = captureSavedOptionOrder();
        assertEquals("STOP", capturedOrder.getType());
    }

    @Test
    public void testBuyOptionLimitOrder() {
        BuyOptionDto buyOptionDto = new BuyOptionDto();
        buyOptionDto.setEmployeeId(1L);
        buyOptionDto.setContractSymbol("OPT300");
        buyOptionDto.setPrice(300.0);
        buyOptionDto.setTicker("TICK300");
        buyOptionDto.setStopValue(0.0);
        buyOptionDto.setLimitValue(290.0);

        Actuary actuary = new Actuary(1L, 1L, "user@example.com", "actuary", 5000.0, 0.0, true);
        when(actuaryRepository.findByEmployeeId(1L)).thenReturn(actuary);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrderService.buyOption(buyOptionDto);

        OptionOrder capturedOrder = captureSavedOptionOrder();
        assertEquals("LIMIT", capturedOrder.getType());
    }

    @Test
    public void testBuyOptionStopLimitOrder() {
        BuyOptionDto buyOptionDto = new BuyOptionDto();
        buyOptionDto.setEmployeeId(1L);
        buyOptionDto.setContractSymbol("OPT400");
        buyOptionDto.setPrice(400.0);
        buyOptionDto.setTicker("TICK400");
        buyOptionDto.setStopValue(410.0);
        buyOptionDto.setLimitValue(405.0);

        Actuary actuary = new Actuary(1L, 1L, "user@example.com", "actuary", 5000.0, 0.0, true);
        when(actuaryRepository.findByEmployeeId(1L)).thenReturn(actuary);
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrderService.buyOption(buyOptionDto);

        OptionOrder capturedOrder = captureSavedOptionOrder();
        assertEquals("STOP_LIMIT", capturedOrder.getType());
    }

    @Test
    public void testExecuteTaskWithEmptyList() {
        assertTrue(optionOrderService.ordersToBuy.isEmpty());

        optionOrderService.executeTask();

        assertTrue(optionOrderService.ordersToBuy.isEmpty(), "No processing should occur on empty list");
    }

    @Test
    public void testExecuteTaskWithMarketOrder() {
        optionOrder.setType("MARKET");
        optionOrder.setSymbol("OPT100");
        optionOrder.setPrice(100.0);
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setAsk(101.0);

        optionOrder.setSymbol("OPT100");
        optionOrderService.ordersToBuy.add(optionOrder);

        Option mockedOption = new Option();
        mockedOption.setAsk(101.0);
        mockedOption.setContractSymbol("OPT100");
        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(mockedOption));

        // Execute the task
        optionOrderService.executeTask();

        // Verify interactions
        verify(myOptionService).addAmountToMyOptions(eq("OPT100"), eq(1), eq("TICK100"));

        // Capture the OptionOrder that's saved to check the final state
        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("FINISHED", savedOrder.getStatus(), "The order status should be FINISHED");
        assertTrue(optionOrderService.ordersToBuy.isEmpty(), "The order should be removed from the list");
    }

    @Test
    public void testExecuteTaskWithLimitOrderSuccess() {
        optionOrder.setSymbol("OPT100");
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setContractSymbol("OPT100");
        option.setAsk(101.0);

        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(option));
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrder.setType("LIMIT");
        optionOrder.setLimitValue(102.0);  // Current price is 101.0, limit is 102.0
        optionOrderService.ordersToBuy.add(optionOrder);

        optionOrderService.executeTask();

        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("FINISHED", savedOrder.getStatus());
        assertTrue(optionOrderService.ordersToBuy.isEmpty(), "Order should be removed from list after processing");
        verify(myOptionService).addAmountToMyOptions("OPT100", 1, "TICK100");
    }

    @Test
    public void testExecuteTaskWithLimitOrderFailure() {
        optionOrder.setSymbol("OPT100");
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setContractSymbol("OPT100");
        option.setAsk(101.0);

        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(option));
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrder.setType("LIMIT");
        optionOrder.setLimitValue(100.0);  // Current price is 101.0, limit is 100.0
        optionOrderService.ordersToBuy.add(optionOrder);

        optionOrderService.executeTask();

        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("FAILED", savedOrder.getStatus());
        assertTrue(optionOrderService.ordersToBuy.isEmpty(), "Order should be removed from list after processing");
    }

    @Test
    public void testExecuteTaskWithStopOrderTransitionToMarket() {
        optionOrder.setSymbol("OPT100");
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setContractSymbol("OPT100");
        option.setAsk(101.0);

        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(option));
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrder.setType("STOP");
        optionOrder.setStopValue(100.0);  // Current price is 101.0, stop is 100.0
        optionOrderService.ordersToBuy.add(optionOrder);

        optionOrderService.executeTask();

        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("MARKET", savedOrder.getType());
        verify(optionOrderRepository).save(savedOrder);
    }

    @Test
    public void testExecuteTaskWithStopOrderNoTransition() {
        optionOrder.setSymbol("OPT100");
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setContractSymbol("OPT100");
        option.setAsk(101.0);

        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(option));
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrder.setType("STOP");
        optionOrder.setStopValue(102.0);  // Current price is 101.0, stop is 102.0
        optionOrderService.ordersToBuy.add(optionOrder);

        optionOrderService.executeTask();

        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("STOP", savedOrder.getType());
        assertTrue(optionOrderService.ordersToBuy.contains(savedOrder), "Order should still be in the list");
    }

    @Test
    public void testExecuteTaskWithStopLimitOrderTransitionToLimit() {
        optionOrder.setSymbol("OPT100");
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setContractSymbol("OPT100");
        option.setAsk(101.0);

        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(option));
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrder.setType("STOP_LIMIT");
        optionOrder.setStopValue(100.0);  // Current price is 101.0
        optionOrder.setLimitValue(105.0); // Limit above current price
        optionOrderService.ordersToBuy.add(optionOrder);

        optionOrderService.executeTask();

        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("LIMIT", savedOrder.getType());
        verify(optionOrderRepository).save(savedOrder);
    }

    @Test
    public void testExecuteTaskWithStopLimitOrderNoTransition() {
        optionOrder.setSymbol("OPT100");
        optionOrder.setTicker("TICK100");

        Option option = new Option();
        option.setContractSymbol("OPT100");
        option.setAsk(101.0);

        when(optionRepository.findByContractSymbol("OPT100")).thenReturn(Optional.of(option));
        when(optionOrderRepository.save(any(OptionOrder.class))).thenAnswer(i -> i.getArgument(0));

        optionOrder.setType("STOP_LIMIT");
        optionOrder.setStopValue(102.0);  // Current price is 101.0, stop is 102.0
        optionOrder.setLimitValue(105.0); // Limit above stop value
        optionOrderService.ordersToBuy.add(optionOrder);

        optionOrderService.executeTask();

        OptionOrder savedOrder = captureSavedOptionOrder();
        assertEquals("STOP_LIMIT", savedOrder.getType());
        assertTrue(optionOrderService.ordersToBuy.contains(savedOrder), "Order should still be in the list");
    }

}
