package rs.edu.raf.exchangeservice.service.listingService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.BankServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.CompanyAccountDto;
import rs.edu.raf.exchangeservice.domain.dto.buySell.BuyFutureCompanyDto;
import rs.edu.raf.exchangeservice.domain.dto.listing.FutureDto;
import rs.edu.raf.exchangeservice.domain.mappers.FutureMapper;
import rs.edu.raf.exchangeservice.domain.model.enums.BankCertificate;
import rs.edu.raf.exchangeservice.domain.model.enums.SellerCertificate;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.myListing.FutureContract;
import rs.edu.raf.exchangeservice.repository.FutureContractRepository;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FutureService {
    private final FutureRepository futureRepository;
    private final BankServiceClient bankServiceClient;
    private final FutureContractRepository futureContractRepository;

    //    private final String pathToFile = "exchange-service/src/main/resources/data/future_data.csv";
    //@PostConstruct
    public void loadData(){
        if(futureRepository.count() > 0){
            return;
        }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("future_data.csv").getInputStream()))) {
                String line;
                // Skip the header line
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    Future future = new Future();
                    future.setContractName(parts[0]);
                    future.setContractSize(Integer.parseInt(parts[1]));
                    future.setContractUnit(parts[2]);
                    future.setMaintenanceMargin(Integer.parseInt(parts[3]));
                    future.setType(parts[4]);
                    Random random = new Random();
                    int min = 10000;
                    int max = 50000;
                    double price = random.nextInt((max - min) + 1) + min;

                    future.setPrice(price);
                    future.setCurrencyMark("RSD");

                    this.futureRepository.save(future); //save in DB
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    public List<Future> findAll(){
        return this.futureRepository.findAll();
    }

    public FutureDto findByContractName(String contractName){
        Optional<Future> future = futureRepository.findByContractName(contractName);
        return future.map(FutureMapper.INSTANCE::futureToFutureDto).orElse(null);
    }

    public boolean requestToBuyFutureByCompany(BuyFutureCompanyDto dto){
//        ResponseEntity<?> entity = bankServiceClient.getByCompanyId(dto.getBuyerId());
//        CompanyAccountDto acc = (CompanyAccountDto) entity.getBody();
//
//
//        if(acc.getAvailableBalance().compareTo(dto.getPrice()) < 0){
//            return false;
//        }
        //TODO: proveriti na bank servisu da li ima dovoljno novca
        FutureContract contract = new FutureContract();
        contract.setCompanyBuyerId(dto.getBuyerId());
        contract.setCompanySellerId(dto.getSellerId());
        contract.setContractName(dto.getContractName());
        contract.setPrice(dto.getPrice());
        contract.setBankCertificate(BankCertificate.PROCESSING);
        contract.setSellerCertificate(SellerCertificate.PROCESSING);
        contract.setDateCreated(System.currentTimeMillis());
        futureContractRepository.save(contract);
        return true;
    }
}
