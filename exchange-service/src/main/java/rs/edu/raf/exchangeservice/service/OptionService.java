package rs.edu.raf.exchangeservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.Forex;
import rs.edu.raf.exchangeservice.domain.model.Option;
import rs.edu.raf.exchangeservice.domain.model.Ticker;
import rs.edu.raf.exchangeservice.repository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.TickerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionsRepository;
    private final TickerRepository tickerRepository;
    private final String apiCall = "https://query1.finance.yahoo.com/v6/finance/options/";

    public void loadData() throws JsonProcessingException {
        List<Ticker> tickersList = tickerRepository.findAll();

        for (Ticker ticker : tickersList) {
            ResponseEntity<?> response = null;
            try {
                RestTemplate restTemplate = new RestTemplate();
                response = restTemplate.exchange(
                        apiCall + ticker.getTicker(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<String>() {
                        });

            } catch (Exception e) {
                //DO NOTHING
            }

            if (response != null) {
                if (!(response.getStatusCode().is4xxClientError()) || !(response.getStatusCode().is5xxServerError())) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonResponse = (String) response.getBody();
                    JsonNode rootNode = objectMapper.readTree(jsonResponse);

                    JsonNode optionNode = rootNode.path("optionChain").path("result").get(0).path("options");
                    if (optionNode.isEmpty() || optionNode.isNull()) {
                        continue;
                    }

                    JsonNode calls = rootNode.path("optionChain").path("result").get(0).path("options").get(0).path("calls");
                    if (!calls.isEmpty()) {
                        saveOptions(calls, "Calls", ticker.getTicker());
                    }

                    JsonNode puts = rootNode.path("optionChain").path("result").get(0).path("options").get(0).path("puts");
                    if (!puts.isEmpty()) {
                        saveOptions(puts, "Puts", ticker.getTicker());
                    }
                }
            }
        }
    }

    private void saveOptions(JsonNode jsonNode, String type, String stockListing) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (JsonNode node : jsonNode) {
            try {
                Option options = objectMapper.treeToValue(node, Option.class);
                options.setOptionType(type);
                options.setLastRefresh(System.currentTimeMillis());
                options.setStockListing(stockListing);
                this.optionsRepository.save(options);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Option> findCalls(String ticker){
        return this.optionsRepository.findByStockListingAndOptionType(ticker, "Calls");
    }

    public List<Option> findPuts(String ticker){
        return this.optionsRepository.findByStockListingAndOptionType(ticker, "Puts");
    }

    public List<Option> findAllRefreshed() throws JsonProcessingException {
        this.optionsRepository.deleteAll();
        loadData();
        return this.optionsRepository.findAll();
    }
}
