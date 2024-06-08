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
import rs.edu.raf.exchangeservice.domain.model.history.StockIntraday;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.historyRepository.StockIntradayRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StockIntradayService {
    private final StockIntradayRepository stockIntradayRepository;
    private final TickerRepository tickerRepository;
    private final String apiCall = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=";
    private final String apiKey = "&apikey=NMBJV9I1JXOWWEZ6";

    @ExcludeFromJacocoGeneratedReport
    public void loadData() throws JsonProcessingException {
        if(stockIntradayRepository.count() > 0){
            stockIntradayRepository.deleteAll();
        }

        List<Ticker> tickerList = tickerRepository.findAll();

        for (Ticker ticker : tickerList) {
            RestTemplate restTemplate = new RestTemplate();
            String url = apiCall + ticker.getTicker() + "&interval=60min" + this.apiKey;
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>() {
                    });

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = response.getBody();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode resultNode = rootNode.path("Time Series (60min)");
            saveData(resultNode, ticker.getTicker());
        }
    }

    @ExcludeFromJacocoGeneratedReport
    public void saveData(JsonNode json, String ticker) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(String.valueOf(json));
        AtomicInteger counter = new AtomicInteger(24);

        rootNode.fields().forEachRemaining(entry -> {
            if (counter.get() == 0){
                return;
            }
            counter.set(counter.get() - 1);
            String date = entry.getKey(); // Date in format "yyyy-MM-dd"
            JsonNode dateNode = entry.getValue();

            String highString = dateNode.get("2. high").asText();
            double high = Double.parseDouble(highString);

            long epochMillis = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .toInstant(ZoneOffset.UTC).toEpochMilli(); //+ 108000000L

            StockIntraday stockDaily = new StockIntraday();
            stockDaily.setDate(epochMillis + 18 * 60 * 60 * 1000);
            stockDaily.setPrice(high);
            stockDaily.setTicker(ticker);
            stockIntradayRepository.save(stockDaily);
        });
    }

    public List<StockIntraday> findByTicker(String ticker){
        return this.stockIntradayRepository.findByTicker(ticker);
    }
}
