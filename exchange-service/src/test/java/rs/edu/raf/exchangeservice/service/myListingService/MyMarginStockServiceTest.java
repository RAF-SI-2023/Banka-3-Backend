package rs.edu.raf.exchangeservice.service.myListingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyMarginStock;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyMarginStockRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyMarginStockServiceTest {

    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private MyMarginStockRepository myMarginStockRepository;

    @InjectMocks
    private MyMarginStockService myMarginStockService;


    @Test
    public void testFindAllByUserId() {
        Long userId = 1L;
        MyMarginStock stock1 = new MyMarginStock(1L, "AAPL", userId, 1L, 100, "USD", 150.0, 1);
        MyMarginStock stock2 = new MyMarginStock(2L, "GOOGL", userId, 2L, 50, "USD", 200.0, 1);
        List<MyMarginStock> expectedStocks = Arrays.asList(stock1, stock2);

        when(myMarginStockRepository.findAllByUserId(userId)).thenReturn(expectedStocks);

        List<MyMarginStock> actualStocks = myMarginStockService.findAllByUserId(userId);

        assertEquals(expectedStocks.size(), actualStocks.size());
        assertEquals(expectedStocks.get(0), actualStocks.get(0));
        assertEquals(expectedStocks.get(1), actualStocks.get(1));
    }

    @Test
    public void testFindAllByCompanyId() {
        Long companyId = 1L;
        MyMarginStock stock1 = new MyMarginStock(1L, "AAPL", 1L, companyId, 100, "USD", 150.0, 1);
        MyMarginStock stock2 = new MyMarginStock(2L, "GOOGL", 2L, companyId, 50, "USD", 200.0, 1);
        List<MyMarginStock> expectedStocks = Arrays.asList(stock1, stock2);

        when(myMarginStockRepository.findAllByCompanyId(companyId)).thenReturn(expectedStocks);

        List<MyMarginStock> actualStocks = myMarginStockService.findAllByCompanyId(companyId);

        assertEquals(expectedStocks.size(), actualStocks.size());
        assertEquals(expectedStocks.get(0), actualStocks.get(0));
        assertEquals(expectedStocks.get(1), actualStocks.get(1));
    }

    @Test
    public void testAddAmountToMyMarginStock_WithUserId() {
        String ticker = "AAPL";
        Integer amount = 50;
        Long userId = 1L;
        Double minimumPrice = 150.0;

        // Mocking the behavior of repositories
        when(myMarginStockRepository.findByTickerAndUserId(ticker, userId)).thenReturn(null);

        Ticker tickerEntity = new Ticker();
        tickerEntity.setTicker(ticker);
        tickerEntity.setCurrencyName("USD");
        when(tickerRepository.findByTicker(ticker)).thenReturn(tickerEntity);

        // Calling the method to test
        myMarginStockService.addAmountToMyMarginStock(ticker, amount, userId, null, minimumPrice);

        // Verifying that save was called with the correct arguments
        verify(myMarginStockRepository).save(any(MyMarginStock.class));
    }

    @Test
    public void testAddAmountToMyMarginStock_WithCompanyId() {
        String ticker = "GOOGL";
        Integer amount = 100;
        Long companyId = 2L;
        Double minimumPrice = 200.0;

        // Mocking the behavior of repositories
        when(myMarginStockRepository.findByTickerAndUserId(ticker, null)).thenReturn(null);

        Ticker tickerEntity = new Ticker();
        tickerEntity.setTicker(ticker);
        tickerEntity.setCurrencyName("USD");
        when(tickerRepository.findByTicker(ticker)).thenReturn(tickerEntity);

        // Calling the method to test
        myMarginStockService.addAmountToMyMarginStock(ticker, amount, null, companyId, minimumPrice);

        // Verifying that save was called with the correct arguments
        verify(myMarginStockRepository).save(any(MyMarginStock.class));
    }
    @Test
    public void testRemoveAmountFromMyMarginStock_WithUserId() {
        String ticker = "AAPL";
        Integer amount = 50;
        Long userId = 1L;

        MyMarginStock existingStock = new MyMarginStock();
        existingStock.setTicker(ticker);
        existingStock.setUserId(userId);
        existingStock.setCompanyId(null);
        existingStock.setAmount(100);

        // Mocking the behavior of repository
        when(myMarginStockRepository.findByTickerAndUserId(ticker, userId)).thenReturn(existingStock);

        // Calling the method to test
        myMarginStockService.removeAmountFromMyMarginStock(ticker, amount, userId, null);

        // Verifying that save or delete was called based on the amount

        verify(myMarginStockRepository).save(existingStock);
    }

    @Test
    public void testRemoveAmountFromMyMarginStock_WithCompanyId() {
        String ticker = "GOOGL";
        Integer amount = 100;
        Long companyId = 2L;

        MyMarginStock existingStock = new MyMarginStock();
        existingStock.setTicker(ticker);
        existingStock.setUserId(null);
        existingStock.setCompanyId(companyId);
        existingStock.setAmount(150);

        // Mocking the behavior of repository
        when(myMarginStockRepository.findByTickerAndCompanyId(ticker, companyId)).thenReturn(existingStock);

        // Calling the method to test
        myMarginStockService.removeAmountFromMyMarginStock(ticker, amount, null, companyId);

        // Verifying that save or delete was called based on the amount
        verify(myMarginStockRepository).save(existingStock);

    }

    @Test
    public void testRemoveAmountFromMyMarginStock_StockNotFound() {
        String ticker = "AAPL";
        Integer amount = 50;
        Long userId = 1L;

        // Mocking the behavior of repository
        when(myMarginStockRepository.findByTickerAndUserId(ticker, userId)).thenReturn(null);

        // Calling the method to test
        myMarginStockService.removeAmountFromMyMarginStock(ticker, amount, userId, null);

        // Verifying that no save or delete was called
        verify(myMarginStockRepository, never()).save(any());
        verify(myMarginStockRepository, never()).delete(any());
    }
}