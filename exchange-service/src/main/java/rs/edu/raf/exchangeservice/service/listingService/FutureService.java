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
                    if (parts[4].equals("bushel")) {
                        future.setPrice(10.5); // Example price for a bushel
                    } else if (parts[4].equals("pound")) {
                        future.setPrice(1.2); // Example price for a pound
                    } else if (parts[4].equals("board feet")) {
                        future.setPrice(2.8); // Example price for board feet
                    } else if (parts[4].equals("barrel")) {
                        future.setPrice(65.0); // Example price for a barrel
                    } else if (parts[4].equals("MMBtu")) {
                        future.setPrice(3.1); // Example price for MMBtu
                    } else if (parts[4].equals("gallon")) {
                        future.setPrice(3.5); // Example price for a gallon
                    } else if (parts[4].equals("ounce")) {
                        future.setPrice(0.05); // Example price for an ounce
                    } else if (parts[4].equals("troy ounce")) {
                        future.setPrice(2000.0); // Example price for a troy ounce
                    } else if (parts[4].equals("metric ton")) {
                        future.setPrice(500.0); // Example price for a metric ton
                    } else {
                        future.setPrice(123.0); // Example price for a metric ton
                    }
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
