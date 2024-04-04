package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.dto.StockDto;
import rs.edu.raf.exchangeservice.domain.mappers.StockMapper;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.helper.GlobalQuote;
import rs.edu.raf.exchangeservice.repository.listing.StockRepository;
import rs.edu.raf.exchangeservice.repository.listing.TickerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final TickerRepository tickerRepository;
    private final String api = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=";
    private final String apiKey = "&apikey=NMBJV9I1JXOWWEZ6";

    public void loadData() {
        List<Ticker> tickersList = tickerRepository.findAll();

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

    public List<StockDto> findAll(){
        return this.stockRepository.findAll().stream().map(
                StockMapper.INSTANCE::stockToStockDto).collect(Collectors.toList());
    }

    public StockDto findByTicker(String ticker){
        Optional<Stock> stock = stockRepository.findByTicker(ticker);
        return stock.map(StockMapper.INSTANCE::stockToStockDto).orElse(null);
    }

    public List<Stock> findAllRefreshed() throws JsonProcessingException {
        this.stockRepository.deleteAll();
        loadData();
        return this.stockRepository.findAll();
    }
}
