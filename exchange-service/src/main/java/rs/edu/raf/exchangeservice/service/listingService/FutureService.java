package rs.edu.raf.exchangeservice.service.listingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.repository.listingRepository.FutureRepository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FutureService {
    private final FutureRepository futureRepository;
    private final String pathToFile = "exchange-service/src/main/resources/data/future_data.csv";

    @PostConstruct
    public void loadData(){
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
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

                this.futureRepository.save(future); //save in DB
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Future> findAll(){
        return this.futureRepository.findAll();
    }
}
