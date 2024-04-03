package rs.edu.raf.exchangeservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.helper.Result;
import rs.edu.raf.exchangeservice.repository.TickerRepository;
import rs.edu.raf.exchangeservice.service.historyService.StockDailyService;
import rs.edu.raf.exchangeservice.service.historyService.StockIntradayService;
import rs.edu.raf.exchangeservice.service.historyService.StockMonthlyService;
import rs.edu.raf.exchangeservice.service.historyService.StockWeeklyService;
import rs.edu.raf.exchangeservice.service.myListingsService.MyStockService;

import javax.annotation.PostConstruct;

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
    private final String TickerURL = "https://api.polygon.io/v3/reference/tickers?active=true&apiKey=RTKplv_CDK1Lh7kx0yPTPEsaqUy14wiT";

    @PostConstruct
    public void loadData() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result> response = restTemplate.exchange(
                TickerURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {});

        tickerRepository.save((response.getBody()).getTickers().get(0));
//        tickerRepository.saveAll((response.getBody()).getTickers()); //TODO: otkomentarisati
        stockService.loadData();
        optionService.loadData();
        stockDailyService.loadData();
        stockIntradayService.loadData();
        stockMonthlyService.loadData();
        stockWeeklyService.loadData();
        myStockService.loadData();
    }
}
