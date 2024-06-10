package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.configuration.option.OptionUpdateEvent;
import rs.edu.raf.exchangeservice.domain.dto.CompanyAccountDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.BankTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyOptionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionsRepository;
    private final MyOptionRepository myOptionRepository;
    private final TickerRepository tickerRepository;
    private final ContractRepository contractRepository;
    private final String apiCall = "https://query1.finance.yahoo.com/v6/finance/options/";
    private final BankServiceClient bankServiceClient;
    private final ApplicationEventPublisher eventPublisher;

    @ExcludeFromJacocoGeneratedReport
    public void loadData() throws JsonProcessingException {
        if(optionsRepository.count() > 0){
            optionsRepository.deleteAll();
        }

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


    public MyOption buyOptionsFromExchange(BuyOptionDto buyOptionDto) {
        Option option = findByContractSymbol(buyOptionDto.getContractSymbol());
        if(option == null)
            throw new RuntimeException("Option not found");

        int openInterest = option.getOpenInterest();
        int quantity = buyOptionDto.getQuantity();
        double bid = option.getBid();

        //provera da li na berzi ima dovoljno
        if(quantity > openInterest)
            throw new RuntimeException("Not enough options available for purchase");

        BankTransactionDto bankTransactionDto = new BankTransactionDto();
        bankTransactionDto.setCompanyId(buyOptionDto.getCompanyId());

        bankTransactionDto.setUserId(null);
        bankTransactionDto.setCurrencyMark(option.getCurrencyMark());
        bankTransactionDto.setAmount(bid * quantity);

        ResponseEntity<?> responseEntity = bankServiceClient.stockBuyTransaction(bankTransactionDto);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            //oduzeti quantity sa berze
            option.setOpenInterest(openInterest - quantity);
            this.optionsRepository.save(option);
            //dodati quantity u MyOption (po potrebi napraviti MyOption)
            MyOption myOption = this.myOptionRepository.findByContractSymbol(option.getContractSymbol());
            if(myOption == null) {
                myOption = new MyOption();
                myOption.setCompanyId(buyOptionDto.getCompanyId());
                myOption.setContractSymbol(option.getContractSymbol());
                myOption.setOptionType(option.getOptionType());
                myOption.setCurrencyMark(option.getCurrencyMark());
                myOption.setPrice(option.getPrice());
                myOption.setAsk(option.getAsk());
                myOption.setBid(option.getBid());
                myOption.setQuantity(0);
            }

            myOption.setQuantity(myOption.getQuantity() + quantity);
            myOptionRepository.save(myOption);
            eventPublisher.publishEvent(new OptionUpdateEvent(this, myOption));
            return myOption;
        }

        return null;
    }


    private void saveOptions(JsonNode jsonNode, String type, String stockListing) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (JsonNode node : jsonNode) {
            try {
                Option options = objectMapper.treeToValue(node, Option.class);
                options.setOptionType(type);
                options.setLastRefresh(System.currentTimeMillis());
                options.setStockListing(stockListing);
                options.setCurrencyMark("USD");
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

    public Option findByContractSymbol(String contractSymbol) {
        return this.optionsRepository.findByContractSymbol(contractSymbol);
    }

    public List<Option> findAllRefreshed() throws JsonProcessingException {
        this.optionsRepository.deleteAll();
        loadData();
        return this.optionsRepository.findAll();
    }
}
