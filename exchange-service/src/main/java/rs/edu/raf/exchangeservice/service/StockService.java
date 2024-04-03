package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.Ticker;
import rs.edu.raf.exchangeservice.domain.model.helper.GlobalQuote;
import rs.edu.raf.exchangeservice.repository.StockRepository;
import rs.edu.raf.exchangeservice.repository.TickerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final TickerRepository tickerRepository;
    private final String api = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=";
    private final String apiKey = "&apikey=NMBJV9I1JXOWWEZ6";

    protected void loadData() {
//        List<Ticker> tickersList = tickerRepository.findAll(); //TODO:otkomentarisati kada se bude prezentovalo
        List<Ticker> tickersList = List.of(tickerRepository.findByTicker("A")); //da ne bi trosili API pozive, radimo samo sa jednim Tickerom

        for (Ticker ticker : tickersList){
            RestTemplate restTemplate = new RestTemplate();
            String apiCall = api + ticker.getTicker() + this.apiKey;
            ResponseEntity<GlobalQuote> response = restTemplate.exchange(
                    apiCall,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GlobalQuote>() {});
            //spajamo Stock za Ticker
            response.getBody().getStock().setName(ticker.getName());
            response.getBody().getStock().setTicker(ticker.getTicker());
            response.getBody().getStock().setExchange(ticker.getPrimaryExchange());
            response.getBody().getStock().setLastRefresh(System.currentTimeMillis());

            stockRepository.save(response.getBody().getStock());
        }
    }
}
