package rs.edu.raf.exchangeservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Bank4Stock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.domain.model.offer.OfferStatus;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.listingRepository.Bank4StockRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.MyOfferRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.OfferRepository;
import rs.edu.raf.exchangeservice.service.myListingService.MyStockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class Banka4OtcService {

    private final OfferRepository offerRepository;

    private final MyStockRepository myStockRepository;

    private final Bank4StockRepository bank4StockRepository;

    private final MyOfferRepository myOfferRepository;

    private final BankServiceClient bankServiceClient;

    private final MyStockService myStockService;

    private static final String URL_TO_BANK4 =  "https://banka-4.si.raf.edu.rs/berza-service/api";



    public List<Offer> findAllOffers(){
        return offerRepository.findAll();
    }

    public List<MyStockDto> findAllStocks(){
        List<MyStock> myStocks =  myStockRepository.findAllByCompanyIdAndPublicAmountGreaterThan(1L, 0);
        List<MyStockDto> dtos = new ArrayList<>();
        for(MyStock myStock: myStocks){
            MyStockDto dto = new MyStockDto();
            dto.setAmount(myStock.getPublicAmount());
            dto.setTicker(myStock.getTicker());
            dtos.add(dto);
        }
        return dtos;
    }

    public Offer receiveOffer(OfferDto offerDto){
        Offer offer = new Offer();
        offer.setIdBank4(offerDto.getIdBank4());
        offer.setAmount(offerDto.getAmount());
        offer.setTicker(offerDto.getTicker());
        offer.setPrice(offerDto.getPrice());

        MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer.getTicker(), 1l);
        if(myStock.getPublicAmount() >= offer.getAmount() && offer.getAmount() >= 0) {
            offer.setOfferStatus(OfferStatus.PROCESSING);
        } else {
            offer.setOfferStatus(OfferStatus.DECLINED);
        }
        offerRepository.save(offer);

        return offer;
    }

    //kad mi prihvatamo njihovu ponudu
    public Offer acceptOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.ACCEPTED);
            offerRepository.save(offer1);
            MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer1.getTicker(), 1l); // ovde
            myStock.setAmount(myStock.getAmount() - offer1.getAmount());
            myStock.setPublicAmount(myStock.getPublicAmount() - offer1.getAmount());
            myStockRepository.save(myStock);

            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setCompanyFromId(5l);
            companyOtcDto.setCompanyToId(1l);
            companyOtcDto.setAmount(offer1.getPrice().doubleValue());
            companyOtcDto.setTax(0.0);

            bankServiceClient.otcBank4transaction(companyOtcDto);

            offerRepository.save(offer1);
            return offer1;
        }
        return null;
    }
    public Offer declineOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.DECLINED);
            offerRepository.save(offer1);
            return offer1;
        }
        return null;
    }

    public List<MyStockDto> getAllStocksForBank4(){
        List<Bank4Stock> myStock = bank4StockRepository.findAll();
        List<MyStockDto> dtos = new ArrayList<>();
        for(Bank4Stock myStock1: myStock){
            MyStockDto dto = new MyStockDto();
            dto.setAmount(myStock1.getAmount());
            dto.setTicker(myStock1.getTicker());
            dtos.add(dto);
        }
        return dtos;
    }

//    @Async
    @Scheduled(fixedRate = 60000)
    @ExcludeFromJacocoGeneratedReport
    public List<MyStockDto> getBank4Stocks(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            //ide njihov url, ovo sam stavila samo za testiranje
            //todo proveriti tacan url
            String url = URL_TO_BANK4 + "/user-stocks/get-our-banks-stocks";
            //String url ="http://localhost:8083/api/v1/otcTrade/getStocks";
            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });
            bank4StockRepository.deleteAll();
            List<MyStockDto> dtos = response.getBody();
            for(MyStockDto myStockDto: dtos){

                Bank4Stock stock = null;
                stock = bank4StockRepository.findByTicker(myStockDto.getTicker());
                if(stock == null){
                    stock = new Bank4Stock();
                    stock.setTicker(myStockDto.getTicker());
                    stock.setAmount(myStockDto.getAmount());
                    bank4StockRepository.save(stock);
                }
            }
            return dtos;
        } catch (Exception e){
            System.out.println("ne radi banka 4");;
        }

        return null;
    }

   // @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    private void sendAcceptedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.ACCEPTED);
        if(!offers.isEmpty()){
            for(Offer offer:offers){
                /// TODO: proveriti tacan url
                String url = URL_TO_BANK4 + "/offer/accept-our-offer/" + offer.getIdBank4();
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<String>() {
                });
                offer.setOfferStatus(OfferStatus.FINISHED);
                offerRepository.save(offer);
            }
        }
    }

    // @Scheduled(fixedRate = 10000)
    private void sandDeclinedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.DECLINED);
        if(!offers.isEmpty()){
            for(Offer offer:offers){
                /// TODO: proveriti tacan url
                String url = URL_TO_BANK4 + "/offer/decline-our-offer/" + offer.getIdBank4();
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        null,
                        new ParameterizedTypeReference<String>() {
                        });
                offer.setOfferStatus(OfferStatus.FINISHED);
                offerRepository.save(offer);
            }
        }
    }
        @ExcludeFromJacocoGeneratedReport
    public MyOffer makeOffer(FrontendOfferDto frontendOfferDto){
        MyOffer myOffer = new MyOffer();
        myOffer.setAmount(frontendOfferDto.getAmount());
        myOffer.setTicker(frontendOfferDto.getTicker());
        myOffer.setPrice(frontendOfferDto.getPrice());
        myOffer.setOfferStatus(OfferStatus.PROCESSING);
        MyOffer myOffer1 = myOfferRepository.save(myOffer);

        //todo proveriti tacan url
        String url = URL_TO_BANK4 + "/offer/place-offer";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Gson gson = new Gson();

        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(myOffer1), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {
                });


        return myOffer1;
    }
git
    public MyOffer offerAccepted(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);
        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.ACCEPTED);
            if(myStockRepository.findByTicker(myOffer.getTicker()) == null) {
                MyStock myStock = new MyStock();
                myStock.setTicker(myOffer.getTicker());
                myStock.setCompanyId(1l);
                myStock.setAmount(myOffer.getAmount());
                myStock.setPrivateAmount(0);
                myStock.setPublicAmount(myOffer.getAmount());
                myStock.setCurrencyMark("RSD");
                double minimumPrice = myOffer.getPrice()/myOffer.getAmount();
                myStock.setMinimumPrice(minimumPrice);

                myStockRepository.save(myStock);

            }else {
                MyStock myStock = myStockRepository.findByTickerAndCompanyId(myOffer.getTicker(), 1l);
                myStock.setAmount(myStock.getAmount() + myOffer.getAmount());
                myStockRepository.save(myStock);
            }


            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setCompanyFromId(1l);
            companyOtcDto.setCompanyToId(5l);
            companyOtcDto.setAmount(myOffer.getPrice().doubleValue());
            companyOtcDto.setTax(0.0);

            bankServiceClient.otcBank4transaction(companyOtcDto);

            myOfferRepository.save(myOffer);
            return myOffer;
        }
        return null;
    }

    public MyOffer offerDeclined(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);
        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.DECLINED);

            myOfferRepository.save(myOffer);
            return myOffer;
        }
        return null;
    }

    public List<MyOffer> getMyOffers(){
        return myOfferRepository.findAll();
    }

}
