package rs.edu.raf.exchangeservice;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.dto.SellOptionDto;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrderSell;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;
import rs.edu.raf.exchangeservice.repository.orderRepository.OptionOrderSellRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyOptionService;


import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyOptionServiceUnitTests {

    @Mock
    private MyOptionRepository myOptionRepository;

    @Mock
    private OptionOrderSellRepository optionOrderSellRepository;

    @InjectMocks
    private MyOptionService myOptionService;

    @BeforeEach
    public void setUp() {
        myOptionService.ordersToSell = new CopyOnWriteArrayList<>();
    }

    @Test
    public void testSellOption_MarketOrder() {
        // Arrange
        SellOptionDto sellOptionDto = new SellOptionDto();
        sellOptionDto.setTicker("AAPL");
        sellOptionDto.setEmployeeId(123L);
        sellOptionDto.setPrice(150.0);
        sellOptionDto.setMargine(true);

        MyOption myOption = new MyOption();
        myOption.setTicker("AAPL");
        when(myOptionRepository.findByTicker("AAPL")).thenReturn(myOption);

        // Act
        String result = myOptionService.sellOption(sellOptionDto);

        // Assert
        assertEquals("UBACENO U ORDER", result);
        verify(optionOrderSellRepository, times(1)).save(any(OptionOrderSell.class));
    }

    @Test
    public void testSellOption_LimitOrder() {
        // Arrange
        SellOptionDto sellOptionDto = new SellOptionDto();
        sellOptionDto.setTicker("AAPL");
        sellOptionDto.setEmployeeId(123L);
        sellOptionDto.setPrice(150.0);
        sellOptionDto.setLimitValue(155.0); // Only limit value set

        MyOption myOption = new MyOption();
        myOption.setTicker("AAPL");
        when(myOptionRepository.findByTicker("AAPL")).thenReturn(myOption);

        // Act
        String result = myOptionService.sellOption(sellOptionDto);

        // Assert
        assertEquals("UBACENO U ORDER", result);
        verify(optionOrderSellRepository, times(1)).save(any(OptionOrderSell.class));
    }


}
