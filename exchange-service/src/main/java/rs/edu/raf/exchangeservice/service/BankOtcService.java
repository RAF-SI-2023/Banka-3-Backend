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
public class BankOtcService {
    private final OfferRepository offerRepository;
    private final MyStockRepository myStockRepository;
    private final BankOTCStockRepository bankOTCStockRepository;
    private final MyOfferRepository myOfferRepository;
    private final BankServiceClient bankServiceClient;

    //URL
    private static final String URL_TO_BANK1 =  "https://banka-1-dev.si.raf.edu.rs/market-servic/api/v1/otcTrade";
    private static final String URL_TO_BANK2 =  "https://banka-2-dev.si.raf.edu.rs/otc-service/api/v1/otcTrade";
    private static final String URL_TO_BANK4 =  "https://banka-4-dev.si.raf.edu.rs/berza-service/api/v1/otcTrade";
    private static final String URL_TO_BANK5 =  "http://host.docker.internal:9999/api/v1/otcTrade";

    //GET: /getOurStocks
    //dohvatamo sve Stocks koje mi nudimo
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

    //POST: /sendOffer/bank
    //primamo ponude od drugih banaka
    public Offer receiveOffer(OfferDto offerDto, Integer owner){
        Offer offer = new Offer();
        offer.setTicker(offerDto.getTicker());
        offer.setAmount(offerDto.getAmount());
        offer.setPrice(offerDto.getPrice());
        offer.setIdBank(offerDto.getIdBank());
        offer.setOwner(owner);

        MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer.getTicker(), 1L);

        //provera da li mi imamo taj Stock
        if(myStock != null && myStock.getPublicAmount() >= offer.getAmount() && offer.getAmount() >= 0) {
            offer.setOfferStatus(OfferStatus.PROCESSING);
        } else {
            offer.setOfferStatus(OfferStatus.DECLINED);
        }

        offerRepository.save(offer);
        return offer;
    }

    //POST: /offerAccepted/bank/{id}
    //stize poruka da su nam prihvatili ponudu
    public boolean offerAccepted(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);

        if(myOfferOptional.isPresent()){
            MyOffer myOffer = myOfferOptional.get();
            myOffer.setOfferStatus(OfferStatus.ACCEPTED);

            //provera ukoliko taj Stock ne postoji kod nas
            if(myStockRepository.findByTicker(myOffer.getTicker()) == null) {
                MyStock myStock = new MyStock();
                myStock.setTicker(myOffer.getTicker());
                myStock.setCompanyId(1L);
                myStock.setAmount(myOffer.getAmount());
                myStock.setPrivateAmount(0);
                myStock.setPublicAmount(myOffer.getAmount());
                myStock.setCurrencyMark("RSD");
                double minimumPrice = myOffer.getPrice()/myOffer.getAmount();
                myStock.setMinimumPrice(minimumPrice);
                myStockRepository.save(myStock);
            }else {
                MyStock myStock = myStockRepository.findByTickerAndCompanyId(myOffer.getTicker(), 1L);
                myStock.setAmount(myStock.getAmount() + myOffer.getAmount());
                myStock.setPublicAmount(myStock.getPublicAmount() + myOffer.getAmount());
                myStockRepository.save(myStock);
            }

            //skidamo pare sa naseg racuna
            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setCompanyFromId(1L);
            companyOtcDto.setCompanyToId(5L);
            companyOtcDto.setAmount(myOffer.getPrice().doubleValue());
            companyOtcDto.setTax(0.0);
            bankServiceClient.otcBank4transaction(companyOtcDto);

            myOfferRepository.save(myOffer);
            return true;
        }

        return false;
    }

    //POST: /offerDeclined/bank/{id}
    //stize poruka da su nam odbili ponudu
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

    /////////////////////////////FRONTEND/////////////////////////////////////////////////

    //GET: /getBanksStocks
    //dohvatamo sve Stocks od drugih banaka
    public List<BankOTCStock> getAllStocksForBanks(){
        return bankOTCStockRepository.findAll();
    }

    //GET: /getOffers
    //pohvatamo sve ponude koje su nam stigle
    public List<Offer> findAllOffers(){
        return offerRepository.findAll();
    }

    //GET: /getOurOffers
    //dohvatamo sve ponude koje smo mi poslali
    public List<MyOffer> getMyOffers(){
        return myOfferRepository.findAll();
    }

    //PUT: /refresh
    //pozivi ka drugim bankama, da uzmemo njihove Stocks
    @ExcludeFromJacocoGeneratedReport
    public void getBankStocks(){
        bankOTCStockRepository.deleteAll();
        getStocksFromBank1();
        getStocksFromBank2();
        getStocksFromBank4();
        getStocksFromBank5();
    }

    @ExcludeFromJacocoGeneratedReport
    private void getStocksFromBank1(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK1 + "/getOurStocks";

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

    @ExcludeFromJacocoGeneratedReport
    private void getStocksFromBank2(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK2 + "/getOurStocks";

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

    @ExcludeFromJacocoGeneratedReport
    private void getStocksFromBank4(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK4 + "/getOurStocks";

            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });

            if (response != null && response.getStatusCode() == HttpStatus.OK){
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

    @ExcludeFromJacocoGeneratedReport
    private void getStocksFromBank5(){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = URL_TO_BANK5 + "/getOurStocks";

            ResponseEntity<List<MyStockDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MyStockDto>>() {
                    });

            if (response != null && response.getStatusCode() == HttpStatus.OK){
                List<MyStockDto> dtos = response.getBody();

                for(MyStockDto myStockDto: dtos){
                    BankOTCStock stock = new BankOTCStock();
                    stock.setOwner(5);
                    stock.setTicker(myStockDto.getTicker());
                    stock.setAmount(myStockDto.getAmount());
                    bankOTCStockRepository.save(stock);
                }
            }else {
                System.out.println("ne radi banka 5");
            }
        } catch (Exception e){
            System.out.println("ne radi banka 5");
        }
    }

    //POST: /makeOffer
    //sa frontenda nam stize ponuda koju treba proslediti
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

        String url = "";

        if (myOffer.getOwner() == 1){
            url += URL_TO_BANK1 + "/sendOffer/bank3";
        }else if (myOffer.getOwner() == 2){
            url += URL_TO_BANK2 + "/sendOffer/bank3";
        }else if (myOffer.getOwner() == 4){
            url += URL_TO_BANK4 + "/sendOffer/bank3";
        }else if (myOffer.getOwner() == 5){
            url += URL_TO_BANK5 + "/sendOffer/bank3";
        }else {
            return false; //nije dobar owner
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<MyOfferDto> requestEntity = new HttpEntity<>(myOfferDto, headers);

        try {
            ResponseEntity<MyOfferDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<MyOfferDto>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK){
                return true;
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

    //POST: /acceptOffer/{id}
    //kad mi prihvatamo njihovu ponudu
    public boolean acceptOffer(Long id){
        Optional<Offer> offer = offerRepository.findById(id);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            offer1.setOfferStatus(OfferStatus.ACCEPTED);
            offerRepository.save(offer1);

            //smanjujemo kolicinu, uzimamo pare
            MyStock myStock = myStockRepository.findByTickerAndCompanyId(offer1.getTicker(), 1L);
            myStock.setAmount(myStock.getAmount() - offer1.getAmount());
            myStock.setPublicAmount(myStock.getPublicAmount() - offer1.getAmount());
            myStockRepository.save(myStock);

            //dodajemo pare na nas racun
            CompanyOtcDto companyOtcDto = new CompanyOtcDto();
            companyOtcDto.setCompanyFromId(5L);
            companyOtcDto.setCompanyToId(1L);
            companyOtcDto.setAmount(offer1.getPrice().doubleValue());
            companyOtcDto.setTax(0.0);
            bankServiceClient.otcBank4transaction(companyOtcDto);

            offerRepository.save(offer1);
            return true;
        }

        return false;
    }

    //POST: /declineOffer/{id}
    //kad mi odbijemo njihovu ponudu
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

    //DELETE: /deleteMyOffer/id
    //kada treba neka nasu ponuda da obrisemo iz baze
    public boolean deleteMyOffer(Long id){
        Optional<MyOffer> myOfferOptional = myOfferRepository.findById(id);
        if(myOfferOptional.isPresent()) {
            MyOffer myOffer = myOfferOptional.get();
            myOfferRepository.delete(myOffer);
            return true;
        }

        return false;
    }

    //DELETE: /deleteOffer/id
    //kada treba neka tudju ponuda da obrisemo iz baze
    public boolean deleteOffer(Long id){
        Optional<Offer> offerOptional = offerRepository.findById(id);
        if(offerOptional.isPresent()) {
            Offer offer = offerOptional.get();
            offerRepository.delete(offer);
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
                    url += URL_TO_BANK1 + "/offerAccepted/bank3/" + offer.getIdBank();
                }else if(offer.getOwner() == 2){
                    url += URL_TO_BANK2 + "/offerAccepted/bank3/" + offer.getIdBank();
                }else if (offer.getOwner() == 4){
                    url += URL_TO_BANK4 + "/offerAccepted/bank3/" + offer.getIdBank();
                }else if (offer.getOwner() == 5){
                    url += URL_TO_BANK5 + "/offerAccepted/bank3/" + offer.getIdBank();
                }else {
                    return;
                }

                try {
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(offer),
                            new ParameterizedTypeReference<String>() {
                            });

                    if (response.getStatusCode() == HttpStatus.OK){
                        //zavrsavamo sa ponudom
                        offer.setOfferStatus(OfferStatus.FINISHED_ACCEPTED);
                        offerRepository.save(offer);
                    }
                }catch (Exception e){
                    return;
                }
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    @ExcludeFromJacocoGeneratedReport
    private void sendDeclinedOffers(){
        List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.DECLINED);
        if(!offers.isEmpty()){
            for(Offer offer : offers){
                String url = "";

                if (offer.getOwner() == 1){
                    url += URL_TO_BANK1 + "/offerDeclined/bank3/" + offer.getIdBank();
                }else if(offer.getOwner() == 2){
                    url += URL_TO_BANK2 + "/offerDeclined/bank3/" + offer.getIdBank();
                }else if (offer.getOwner() == 4){
                    url += URL_TO_BANK4 + "/offerDeclined/bank3/" + offer.getIdBank();
                }else if (offer.getOwner() == 5){
                    url += URL_TO_BANK5 + "/offerDeclined/bank3/" + offer.getIdBank();
                }else {
                    return;
                }

                try {
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            new HttpEntity<>(offer),
                            new ParameterizedTypeReference<String>() {
                            });

                    if (response.getStatusCode() == HttpStatus.OK){
                        //zavrsavamo sa ponudom
                        offer.setOfferStatus(OfferStatus.FINISHED_DECLINED);
                        offerRepository.save(offer);
                    }
                }catch (Exception e){
                    return;
                }
            }
        }
    }

}
