package rs.edu.raf.exchangeservice.service.historyService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.exchangeservice.domain.model.history.StockWeekly;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockWeeklyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockWeeklyServiceTest {
    @Mock
    private StockWeeklyRepository stockWeeklyRepository;

    @InjectMocks
    private StockWeeklyService stockWeeklyService;

    @Test
    public void testFindByTicker() {
        // Priprema
        String ticker = "AAPL";
        List<StockWeekly> expectedStocks = new ArrayList<>();
        expectedStocks.add(new StockWeekly());
        expectedStocks.add(new StockWeekly());

        when(stockWeeklyRepository.findByTicker(ticker)).thenReturn(expectedStocks);

        // Izvršenje
        List<StockWeekly> actualStocks = stockWeeklyService.findByTicker(ticker);

        // Provera
        assertNotNull(actualStocks);
        assertEquals(expectedStocks.size(), actualStocks.size());
        assertEquals(expectedStocks, actualStocks);

        verify(stockWeeklyRepository, times(1)).findByTicker(ticker);
    }
}