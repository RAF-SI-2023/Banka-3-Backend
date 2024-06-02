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
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Bank4Stock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.domain.model.offer.OfferStatus;
import rs.edu.raf.exchangeservice.repository.listingRepository.Bank4StockRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.MyOfferRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.OfferRepository;

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



    public List<Offer> findAllOffers(){
        return offerRepository.findAll();
    }

    public List<MyStockDto> findAllStocks(){
        List<MyStock> myStocks =  myStockRepository.findAllByCompanyId(1L);
        List<MyStockDto> dtos = new ArrayList<>();
        for(MyStock myStock: myStocks){
            MyStockDto dto = new MyStockDto();
            dto.setAmount(myStock.getAmount());
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
        offer.setOfferStatus(OfferStatus.PROCESSING);
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
            MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer1.getTicker(), 1l);
            myStock.setAmount(myStock.getAmount() - offer1.getAmount());
            myStockRepository.save(myStock);

            //TODO povecati nase pare

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

        //TODO povecati nase pare i skinuti stock amount
        return null;
    }

//    @Async
    public List<MyStockDto> getBank4Stocks(){
        RestTemplate restTemplate = new RestTemplate();
        //ide njihov url, ovo sam stavila samo za testiranje
        String url = "http://localhost:8083/api/v1/otcTrade/getStocks";
        ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MyStockDto>>() {
                });
        bank4StockRepository.deleteAll();
        List<MyStockDto> dtos = response.getBody();
        for(MyStockDto myStockDto: dtos){
            Bank4Stock stock = new Bank4Stock();
            stock.setTicker(myStockDto.getTicker());
            stock.setAmount(myStockDto.getAmount());
            bank4StockRepository.save(stock);
        }
        return dtos;
    }

    @Scheduled(fixedRate = 10000)
    private void sendAcceptedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.ACCEPTED);
        if(!offers.isEmpty()){
            for(Offer offer:offers){
                String url = "njihov url/" + offer.getIdBank4();
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<String>() {
                });
                offer.setOfferStatus(OfferStatus.FINISHED);
                offerRepository.save(offer);
                //TODO treba skidati pare
            }
        }
    }

    public MyOffer makeOffer(FrontendOfferDto frontendOfferDto){
        MyOffer myOffer = new MyOffer();
        myOffer.setAmount(frontendOfferDto.getAmount());
        myOffer.setTicker(frontendOfferDto.getTicker());
        myOffer.setPrice(frontendOfferDto.getPrice());
        myOffer.setOfferStatus(OfferStatus.PROCESSING);
        MyOffer myOffer1 = myOfferRepository.save(myOffer);

        String url = "njihov url/";
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

                myStockRepository.save(myStock);

            }else {
                MyStock myStock = myStockRepository.findByTickerAndCompanyId(myOffer.getTicker(), 1l);
                myStock.setAmount(myStock.getAmount() + myOffer.getAmount());
                myStock.setPublicAmount(myStock.getPublicAmount() + myOffer.getAmount());
            }
            //TODO skinuti pare


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
