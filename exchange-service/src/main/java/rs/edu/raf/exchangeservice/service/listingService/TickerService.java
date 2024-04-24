package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;
import rs.edu.raf.exchangeservice.service.historyService.StockMonthlyService;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TickerService {
    private final ExchangeRepository exchangeRepository;
    private final TickerRepository tickerRepository;
    private final StockService stockService;
    private final OptionService optionService;
    private final StockIntradayService stockIntradayService;
    private final StockDailyService stockDailyService;
    private final StockWeeklyService stockWeeklyService;
    private final StockMonthlyService stockMonthlyService;
    private final MyStockService myStockService;
    //private final String TickerURL = "https://api.polygon.io/v3/reference/tickers?active=true&apiKey=RTKplv_CDK1Lh7kx0yPTPEsaqUy14wiT";

    public void loadData() throws JsonProcessingException {
        Map<String, String> tickers = new HashMap<>();
        tickers.put("AAPL", "Apple Inc.");
        tickers.put("AMD", "Advanced Micro Devices");
        tickers.put("AMZN", "Amazon Inc.");
        tickers.put("IBM", "International Business Machines");
        tickers.put("NVDA", "Nvidia");
        tickers.put("MSFT", "Microsoft");
        tickers.put("TSLA", "Tesla Inc.");
        tickers.put("META", "Meta Inc.");
        tickers.put("GOOGL", "Google Inc.");
        tickers.put("INTC", "Intel");
        tickers.put("T", "AT&T Inc");
        tickers.put("F", "Ford");

        List<Exchange> exchanges = exchangeRepository.findAll();

        for (Map.Entry<String, String> entry : tickers.entrySet()) {
            Ticker ticker = new Ticker();
            ticker.setTicker(entry.getKey());
            ticker.setName(entry.getValue());

            Random random = new Random();
            int randomIndex = random.nextInt(exchanges.size());
            Exchange exchange = exchanges.get(randomIndex);
            ticker.setPrimaryExchange(exchange.getExchange());
            ticker.setCurrencyName("USD");
//            ticker.setCurrencyName(exchange.getCurrency().toUpperCase());
            tickerRepository.save(ticker);
        }

        stockService.loadData();
        optionService.loadData();
        stockIntradayService.loadData();
        stockDailyService.loadData();
        stockWeeklyService.loadData();
        stockMonthlyService.loadData();
        myStockService.loadData();
    }
}
