package rs.edu.raf.exchangeservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Bank4Stock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.repository.listingRepository.Bank4StockRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.OfferRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Banka4OtcService {

    private final OfferRepository offerRepository;

    private final MyStockRepository myStockRepository;

    private final Bank4StockRepository bank4StockRepository;

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

    public void receiveOffer(OfferDto offerDto){
        Offer offer = new Offer();
        offer.setId(offerDto.getId());
        offer.setAmount(offerDto.getAmount());
        offer.setTicker(offerDto.getTicker());
        offer.setPrice(offerDto.getPrice());
        offerRepository.save(offer);
        //ovde treba da zovemo metodu acceptOffer,
        // kako bismo nakon sto dodmo  bazu, obavestili i banku 4
    }

    public void acceptOffer(Long id){
        //poslati id i povecati nase pare i skinuti stock sa naseg stanja
        RestTemplate restTemplate = new RestTemplate();
        String url = "njihov url/" + id;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<String>() {
                });
    }

    //treba da bude void, opet ovo samo za testiranje
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

}
