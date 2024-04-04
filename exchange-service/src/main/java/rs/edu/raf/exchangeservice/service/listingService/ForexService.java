package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.model.listing.Forex;
import rs.edu.raf.exchangeservice.repository.listing.ForexRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ForexService {
    private final ForexRepository forexRepository;
    private final String exchangeApiKey = "96aa86545baf8162d6ecbe21";

    @PostConstruct
    public void loadData() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://v6.exchangerate-api.com/v6/"+exchangeApiKey+"/latest/RSD";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {
                });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = response.getBody();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode resultNode = rootNode.path("conversion_rates");
        saveForex(String.valueOf(resultNode));
    }

    private void saveForex(String jsonNode){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Map<String, Double> forexMap = objectMapper.readValue(jsonNode, new TypeReference<Map<String, Double>>() {});
            // Iterate over the map entries and create Forex objects
            for (Map.Entry<String, Double> entry : forexMap.entrySet()) {
                Forex forex = new Forex();
                forex.setBaseCurrency("RSD");
                forex.setQuoteCurrency(entry.getKey());
                forex.setConversionRate(entry.getValue());
                forex.setLastRefresh(System.currentTimeMillis()); // Set last refresh time

                forexRepository.save(forex);    // Save Forex object to repository
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing forex JSON", e);
        }
    }

    public List<Forex> findAll(){
        return this.forexRepository.findAll();
    }

    public List<Forex> findAllRefreshed() throws JsonProcessingException {
        this.forexRepository.deleteAll();
        loadData();
        return this.forexRepository.findAll();
    }
}
