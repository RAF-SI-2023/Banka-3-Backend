package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.Exchange;
import rs.edu.raf.exchangeservice.domain.model.helper.Result;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.repository.ExchangeRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;
import rs.edu.raf.exchangeservice.service.historyService.StockMonthlyService;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TickerService {
    private final TickerRepository tickerRepository;
    private final StockService stockService;
    private final OptionService optionService;
    private final StockDailyService stockDailyService;
    private final StockIntradayService stockIntradayService;
    private final StockMonthlyService stockMonthlyService;
    private final StockWeeklyService stockWeeklyService;
    private final MyStockService myStockService;
    private final ExchangeRepository exchangeRepository;
    private final String TickerURL = "https://api.polygon.io/v3/reference/tickers?active=true&apiKey=RTKplv_CDK1Lh7kx0yPTPEsaqUy14wiT";

    public void loadData() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result> response = restTemplate.exchange(
                TickerURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {});

        List<Ticker> tickers = Objects.requireNonNull(response.getBody()).getTickers();
        List<Exchange> exchanges = exchangeRepository.findAll();

        for (Ticker ticker : tickers){
            Random random = new Random();
            int randomIndex = random.nextInt(exchanges.size());
            Exchange exchange = exchanges.get(randomIndex);
            ticker.setPrimaryExchange(exchange.getExchange());
            ticker.setCurrencyName(exchange.getCurrency());
            tickerRepository.save(ticker);
            break;
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
