package rs.edu.raf.exchangeservice.service.listingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.CompanyAccountDto;
import rs.edu.raf.exchangeservice.domain.dto.bank.OptionTransactionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyOptionDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyStockCompanyDto;
import rs.edu.raf.exchangeservice.domain.dto.myListing.MyOptionDto;
import rs.edu.raf.exchangeservice.domain.mappers.MyOptionMapper;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Option;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;
import rs.edu.raf.exchangeservice.repository.ContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.OptionRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.TickerRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyOptionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepository optionsRepository;
    private final MyOptionRepository myOptionRepository;
    private final TickerRepository tickerRepository;
    private final ContractRepository contractRepository;
    private final String apiCall = "https://query1.finance.yahoo.com/v6/finance/options/";
    private final BankServiceClient bankServiceClient;


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

    //Kompanija salje zahtev za kupovinu. Pravi se ugovor.Druga firma ima pregled pristiglih ugovora i moze da ih prihvati ili odbije.
    public boolean requestToBuyOptionByCompany(BuyStockCompanyDto buyStockCompanyDto){

        ResponseEntity<?>entity=  bankServiceClient.getByCompanyId(buyStockCompanyDto.getBuyerId());
        CompanyAccountDto companyAccountDto = (CompanyAccountDto) entity.getBody();

        BigDecimal price = buyStockCompanyDto.getPrice();
        Integer amount = buyStockCompanyDto.getAmount();

        if(companyAccountDto.getAvailableBalance().compareTo(price.multiply(BigDecimal.valueOf(amount)))<0){
            return false; //Firma nema dovoljno sredstava
        }

        Contract contract = new Contract();
        contract.setCompanyBuyerId(buyStockCompanyDto.getBuyerId());
        contract.setCompanySellerId(buyStockCompanyDto.getSellerId());
        contract.setTicker(buyStockCompanyDto.getTicker());
        contract.setPrice(buyStockCompanyDto.getPrice());
        contract.setAmount(buyStockCompanyDto.getAmount());
        contract.setBankCertificate(BankCertificate.PROCESSING);
        contract.setSellerCertificate(SellerCertificate.PROCESSING);
        contractRepository.save(contract);
        return true;
    }

    public MyOptionDto buyOptionFromExchange(BuyOptionDto buyOptionDto) {

        Optional<Option> optionalOption = optionsRepository.findById(buyOptionDto.getOptionId());
        if(optionalOption.isEmpty()) {
            throw new RuntimeException("Option with id "+buyOptionDto.getOptionId()+" not found");
        }

        Option option = optionalOption.get();

        ResponseEntity<?>entity = bankServiceClient.getByCompanyId(buyOptionDto.getBuyerId());
        CompanyAccountDto companyAccountDto = (CompanyAccountDto) entity.getBody();

        if(companyAccountDto == null)
            throw new RuntimeException("Company account not found");

        //proveravamo da li firma ima dovljno sredstava
        BigDecimal price = BigDecimal.valueOf(option.getPrice());
        if(companyAccountDto.getAvailableBalance().compareTo(price) < 0) {
            throw new RuntimeException("Company insufficient funds");
        }

        bankServiceClient.optionBuyTransaction(new OptionTransactionDto(companyAccountDto.getAccountId(), option.getPrice()));

        MyOption myOption = new MyOption();
        myOption.setOwnerId(buyOptionDto.getBuyerId());
        myOption.setStockListing(buyOptionDto.getStockListing());
        myOption.setOptionType(buyOptionDto.getOptionType());
        myOption.setStrikePrice(option.getStrikePrice());
        myOption.setSettlementDate(option.getSettlementDate());
        myOptionRepository.save(myOption);

        return MyOptionMapper.INSTANCE.myOptionToMyOptionDto(myOption);
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

    public List<Option> findAllRefreshed() throws JsonProcessingException {
        this.optionsRepository.deleteAll();
        loadData();
        return this.optionsRepository.findAll();
    }
}
