package rs.edu.raf.exchangeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.exchangeservice.client.UserServiceClient;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.repository.ActuaryRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActuaryService {
    private final UserServiceClient userServiceClient;
    private final ActuaryRepository actuaryRepository;

    @PostConstruct
    public void loadActuary(){
        List<ActuaryDto> actuaryDtos = userServiceClient.getEmployees().getBody();
        for (ActuaryDto actuaryDto : actuaryDtos){
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

    public List<Actuary> findAllAgents(){
        return this.actuaryRepository.findByRole("ROLE_AGENT");
    }

    public Actuary restartLimitUsed(Long id){
        Actuary actuary = this.actuaryRepository.findById(id).get();
        actuary.setLimitUsed(0.0);
        return this.actuaryRepository.save(actuary);
    }

    public Actuary setLimit(Long id, Double limit){
        Actuary actuary = this.actuaryRepository.findById(id).get();
        actuary.setLimitValue(limit);
        actuary.setLimitUsed(0.0);
        return this.actuaryRepository.save(actuary);
    }

    public Actuary setOrderRequest(Long id, boolean orderRequest){
        Actuary actuary = this.actuaryRepository.findById(id).get();
        actuary.setOrderRequest(orderRequest);
        return this.actuaryRepository.save(actuary);
    }
}
