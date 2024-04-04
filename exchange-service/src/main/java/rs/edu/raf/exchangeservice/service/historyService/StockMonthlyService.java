package rs.edu.raf.exchangeservice.service.historyService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.history.StockMonthly;
import rs.edu.raf.exchangeservice.repository.listing.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockMonthlyRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StockMonthlyService {
    private final StockMonthlyRepository stockMonthlyRepository;
    private final TickerRepository tickerRepository;
    private final String apiCall = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=";
    private final String apiKey = "&apikey=NMBJV9I1JXOWWEZ6";

    public void loadData() throws JsonProcessingException {
        List<Ticker> tickerList = tickerRepository.findAll();

        for (Ticker ticker : tickerList) {
            RestTemplate restTemplate = new RestTemplate();
            String url = apiCall + ticker.getTicker() + this.apiKey;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {
                    });

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = response.getBody();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode resultNode = rootNode.path("Monthly Time Series");
            saveData(resultNode, ticker.getTicker());
        }
    }

    public void saveData(JsonNode json, String ticker) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(String.valueOf(json));
        AtomicInteger counter = new AtomicInteger(61);

        rootNode.fields().forEachRemaining(entry -> {
            if (counter.get() == 0){
                return;
            }
            counter.set(counter.get() - 1);
            String date = entry.getKey(); // Date in format "yyyy-MM-dd"
            JsonNode dateNode = entry.getValue();

            String highString = dateNode.get("2. high").asText();
            double high = Double.parseDouble(highString);

            long epochMillis = LocalDate.parse(date).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

            StockMonthly stockMonthly = new StockMonthly();
            stockMonthly.setDate(epochMillis);
            stockMonthly.setPrice(high);
            stockMonthly.setTicker(ticker);
            stockMonthlyRepository.save(stockMonthly);
        });
    }

    public List<StockMonthly> findByTicker(String ticker){
        return this.stockMonthlyRepository.findByTicker(ticker);
    }
}
