package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.UserServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.jacoco.ExcludeFromJacocoGeneratedReport;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActuaryService {
    private final UserServiceClient userServiceClient;
    private final ActuaryRepository actuaryRepository;

    @PostConstruct
    @ExcludeFromJacocoGeneratedReport
    public void loadActuary() throws InterruptedException {
        Thread.sleep(5000);
        List<ActuaryDto> actuaryDtoList = userServiceClient.getEmployees().getBody();
        for (ActuaryDto actuaryDto : actuaryDtoList){
            Actuary actuary = new Actuary();
            actuary.setEmployeeId(actuaryDto.getEmployeeId());
            actuary.setRole(actuaryDto.getRole());
            actuary.setEmail(actuaryDto.getEmail());
            if(actuary.getRole().contains("AGENT")){
                actuary.setLimitUsed(0.0);
                actuary.setLimitValue(1000000.0);
                actuary.setOrderRequest(true);
            }else {
                actuary.setLimitUsed(0.0);
                actuary.setLimitValue(0.0);
                actuary.setOrderRequest(false);
            }
            actuaryRepository.save(actuary);
        }
    }

    //vraca listu svih agenata
    public List<Actuary> findAllAgents(){
        return this.actuaryRepository.findByRole("ROLE_AGENT");
    }

    //restartovanje limita Agentu
    @ExcludeFromJacocoGeneratedReport
    public Actuary restartLimitUsed(Long id){
        Actuary actuary = this.actuaryRepository.findByEmployeeId(id);
        actuary.setLimitUsed(0.0);
        return this.actuaryRepository.save(actuary);
    }

    //postavljanje limita agentu
    public Actuary setLimit(Long id, Double limit){
        Actuary actuary = this.actuaryRepository.findById(id).get();
        actuary.setLimitValue(limit);
        actuary.setLimitUsed(0.0);
        return this.actuaryRepository.save(actuary);
    }

    //da li kupovina treba da se odobri
    public Actuary setOrderRequest(Long id, boolean orderRequest){
        Actuary actuary = this.actuaryRepository.findById(id).get();
        actuary.setOrderRequest(orderRequest);
        return this.actuaryRepository.save(actuary);
    }

    //dodajemo novog aktuara, kada se napravi u user service-u
    public void addActuary(ActuaryDto actuaryDto){
        Actuary actuary = new Actuary();
        actuary.setEmployeeId(actuaryDto.getEmployeeId());
        actuary.setRole(actuaryDto.getRole());
        actuary.setEmail(actuaryDto.getEmail());
        if(actuary.getRole().contains("AGENT")){
            actuary.setLimitUsed(0.0);
            actuary.setLimitValue(1000000.0);
            actuary.setOrderRequest(true);
        }else {
            actuary.setLimitUsed(0.0);
            actuary.setLimitValue(0.0);
            actuary.setOrderRequest(false);
        }
        actuaryRepository.save(actuary);
    }

    //scheduler za restartovanje limitUsed zaposlenog
    @Scheduled(cron = "0 59 23 * * *")
    @ExcludeFromJacocoGeneratedReport// Execute at 23:59 every day
    public void myScheduledFunction() {
        actuaryRepository.updateLimitToZeroForAllEmployees();
        System.out.println("Executing function for limitUsed restart");
    }
}
