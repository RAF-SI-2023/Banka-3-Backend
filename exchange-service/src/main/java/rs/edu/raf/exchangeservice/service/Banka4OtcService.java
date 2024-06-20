package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.bank.CompanyOtcDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.FrontendOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyOfferDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.MyStockDto;
import rs.edu.raf.exchangeservice.domain.dto.offer.OfferDto;
import rs.edu.raf.exchangeservice.domain.model.listing.BankOTCStock;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyStock;
import rs.edu.raf.exchangeservice.domain.model.offer.MyOffer;
import rs.edu.raf.exchangeservice.domain.model.offer.Offer;
import rs.edu.raf.exchangeservice.domain.model.offer.OfferStatus;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.listingRepository.BankOTCStockRepository;
import rs.edu.raf.exchangeservice.repository.myListingRepository.MyStockRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.MyOfferRepository;
import rs.edu.raf.exchangeservice.repository.offerRepository.OfferRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Banka4OtcService {
    private final OfferRepository offerRepository;
    private final MyStockRepository myStockRepository;
    private final BankOTCStockRepository bankOTCStockRepository;
    private final MyOfferRepository myOfferRepository;
    private final BankServiceClient bankServiceClient;

    //URL
    private static final String URL_TO_BANK1 =  "https://banka-1-dev.si.raf.edu.rs/";
    private static final String URL_TO_BANK2 =  "https://banka-2-dev.si.raf.edu.rs/";
    private static final String URL_TO_BANK4 =  "https://banka-4-dev.si.raf.edu.rs/berza-service/api";

    //dohvatamo sve Stocks koje mi nudimo
    //GET: /getOurStocks
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

    //primamo ponude od drugih banaka
    //POST: /sendOffer/bank
    public Offer receiveOffer(OfferDto offerDto, Integer owner){
        Offer offer = new Offer();
        offer.setTicker(offerDto.getTicker());
        offer.setAmount(offerDto.getAmount());
        offer.setPrice(offerDto.getPrice());
        offer.setIdBank(offerDto.getIdBank());
        offer.setOwner(owner);

        MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer.getTicker(), 1l);

        //provera da li mi imamo taj Stock
        if(myStock != null && myStock.getPublicAmount() >= offer.getAmount() && offer.getAmount() >= 0) {
            offer.setOfferStatus(OfferStatus.PROCESSING);
        } else {
            offer.setOfferStatus(OfferStatus.DECLINED);
        }

        offerRepository.save(offer);
        return offer;
    }

    //stize poruka da su nam prihvatili ponudu
    //POST: /offerAccepted/bank/{id}
    public boolean offerAccepted(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);

        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.ACCEPTED);

            //provera ukoliko taj Stock ne postoji kod nas
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
                myStock.setPublicAmount(myStock.getPublicAmount() + myOffer.getAmount());
                myStockRepository.save(myStock);
            }

            //skidamo pare sa naseg racuna
            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setCompanyFromId(1l);
            companyOtcDto.setCompanyToId(5l);
            companyOtcDto.setAmount(myOffer.getPrice().doubleValue());
            companyOtcDto.setTax(0.0);
            bankServiceClient.otcBank4transaction(companyOtcDto);

            myOfferRepository.save(myOffer);
            return true;
        }

        return false;
    }

    //stize poruka da su nam odbili ponudu
    //POST: /offerDeclined/bank/{id}
    public boolean offerDeclined(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);
        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.DECLINED);
            myOfferRepository.save(myOffer);
            return true;
        }

        return false;
    }

    ///////////////////////FRONTEND/////////////////////////////////////////////////

    //pohvatamo sve ponude koje su nam stigle
    //GET: /getOffers
    public List<Offer> findAllOffers(){
        return offerRepository.findAll();
    }

    //dohvatamo sve Stocks od drugih banaka
    //GET: /getBanksStocks
    public List<BankOTCStock> getAllStocksForBanks(){
        return bankOTCStockRepository.findAll();
    }

    //pozivi ka drugim bankama, da uzmemo njihove Stocks
    @Scheduled(fixedRate = 30000)
    @ExcludeFromJacocoGeneratedReport
    public void getBankStocks(){
        bankOTCStockRepository.deleteAll();
//        getStocksFromBank1();
//        getStocksFromBank2();
//        getStocksFromBank4();
    }

    private void getStocksFromBank1(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK1 + "";

            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK){
                List<MyStockDto> dtos = response.getBody();

                for(MyStockDto myStockDto: dtos){
                    BankOTCStock stock = new BankOTCStock();
                    stock.setOwner(1);
                    stock.setTicker(myStockDto.getTicker());
                    stock.setAmount(myStockDto.getAmount());
                    bankOTCStockRepository.save(stock);
                }
            }else {
                System.out.println("ne radi banka 1");
            }
        } catch (Exception e){
            System.out.println("ne radi banka 1");
        }
    }

    private void getStocksFromBank2(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK2 + "";

            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK){
                List<MyStockDto> dtos = response.getBody();

                for(MyStockDto myStockDto: dtos){
                    BankOTCStock stock = new BankOTCStock();
                    stock.setOwner(2);
                    stock.setTicker(myStockDto.getTicker());
                    stock.setAmount(myStockDto.getAmount());
                    bankOTCStockRepository.save(stock);
                }
            }else {
                System.out.println("ne radi banka 2");
            }
        } catch (Exception e){
            System.out.println("ne radi banka 2");
        }
    }

    private void getStocksFromBank4(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK4 + "/user-stocks/get-our-banks-stocks";

            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK){
                List<MyStockDto> dtos = response.getBody();

                for(MyStockDto myStockDto: dtos){
                    BankOTCStock stock = new BankOTCStock();
                    stock.setOwner(4);
                    stock.setTicker(myStockDto.getTicker());
                    stock.setAmount(myStockDto.getAmount());
                    bankOTCStockRepository.save(stock);
                }
            }else {
                System.out.println("ne radi banka 4");
            }
        } catch (Exception e){
            System.out.println("ne radi banka 4");
        }
    }

    //dohvatamo sve ponude koje smo mi poslali
    //GET: /getOurOffers
    public List<MyOffer> getMyOffers(){
        return myOfferRepository.findAll();
    }

    //sa frontenda nam stize ponuda koju treba proslediti
    //POST: /makeOffer
    @ExcludeFromJacocoGeneratedReport
    public boolean makeOffer(FrontendOfferDto frontendOfferDto){
        MyOffer myOffer = new MyOffer();
        myOffer.setTicker(frontendOfferDto.getTicker());
        myOffer.setAmount(frontendOfferDto.getAmount());
        myOffer.setPrice(frontendOfferDto.getPrice());
        myOffer.setOwner(frontendOfferDto.getOwner());
        myOffer.setOfferStatus(OfferStatus.PROCESSING);
        MyOffer myOffer1 = myOfferRepository.save(myOffer);

        MyOfferDto myOfferDto = new MyOfferDto();
        myOfferDto.setTicker(myOffer1.getTicker());
        myOfferDto.setAmount(myOffer1.getAmount());
        myOfferDto.setPrice(myOffer1.getPrice());
        myOfferDto.setIdBank(myOffer1.getMyOfferId());

        if (myOffer.getOwner() == 1){
            String url = URL_TO_BANK1 + "";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<MyOfferDto> requestEntity = new HttpEntity<>(myOfferDto, headers);

            ResponseEntity<MyOfferDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<MyOfferDto>() {}
            );
            return true;
        }else if (myOffer.getOwner() == 2){
            String url = URL_TO_BANK2 + "";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<MyOfferDto> requestEntity = new HttpEntity<>(myOfferDto, headers);

            ResponseEntity<MyOfferDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<MyOfferDto>() {}
            );
            return true;
        }else if (myOffer.getOwner() == 4){
            String url = URL_TO_BANK4 + "";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<MyOfferDto> requestEntity = new HttpEntity<>(myOfferDto, headers);

            ResponseEntity<MyOfferDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<MyOfferDto>() {}
            );
            return true;
        }

        return false;
    }

    //kad mi prihvatamo njihovu ponudu
    //POST: /acceptOffer/{id}
    public boolean acceptOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.ACCEPTED);
            offerRepository.save(offer1);

            //smanjujemo kolicinu, uzimamo pare
            MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer1.getTicker(), 1l);
            myStock.setAmount(myStock.getAmount() - offer1.getAmount());
            myStock.setPublicAmount(myStock.getPublicAmount() - offer1.getAmount());
            myStockRepository.save(myStock);

            //dodajemo pare na nas racun
            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setCompanyFromId(5l);
            companyOtcDto.setCompanyToId(1l);
            companyOtcDto.setAmount(offer1.getPrice().doubleValue());
            companyOtcDto.setTax(0.0);
            bankServiceClient.otcBank4transaction(companyOtcDto);

            offerRepository.save(offer1);
            return true;
        }

        return false;
    }

    //kad mi odbijemo njihovu ponudu
    //POST: /declineOffer/{id}
    public boolean declineOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.DECLINED);

            offerRepository.save(offer1);
            return true;
        }

        return false;
    }



    @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    private void sendAcceptedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.ACCEPTED);
        if(!offers.isEmpty()){
            for(Offer offer : offers){
                String url = "";

                if (offer.getOwner() == 1){
                    url += URL_TO_BANK1 + "" + offer.getIdBank();
                }else if(offer.getOwner() == 2){
                    url += URL_TO_BANK2 + "" + offer.getIdBank();
                }else if (offer.getOwner() == 4){
                    url += URL_TO_BANK4 + "/offer/accept-our-offer/" + offer.getIdBank();
                }else {
                    return;
                }

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(offer),
                    new ParameterizedTypeReference<String>() {
                });

                //zavrsavamo sa ponudom
                offer.setOfferStatus(OfferStatus.FINISHED);
                offerRepository.save(offer);
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    private void sandDeclinedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.DECLINED);
        if(!offers.isEmpty()){
            for(Offer offer : offers){
                String url = "";

                if (offer.getOwner() == 1){
                    url += URL_TO_BANK1 + "" + offer.getIdBank();
                }else if(offer.getOwner() == 2){
                    url += URL_TO_BANK2 + "" + offer.getIdBank();
                }else if (offer.getOwner() == 4){
                    url += URL_TO_BANK4 + "/offer/decline-our-offer/" + offer.getIdBank();
                }else {
                    return;
                }

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(offer),
                        new ParameterizedTypeReference<String>() {
                        });

                //zavrsavamo sa ponudom
                offer.setOfferStatus(OfferStatus.FINISHED);
                offerRepository.save(offer);
            }
        }
    }

}
