package rs.edu.raf.exchangeservice.controller;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.model.Actuary;
import rs.edu.raf.exchangeservice.service.ActuaryService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/actuary")
public class ActuaryController {
    private final ActuaryService actuaryService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Actuary>> getAllEmployees(){
        return ResponseEntity.ok(actuaryService.findAllAgents());
    }

    //kada hocemo da zaposlenom restartujemo Limit, posaljemo njegov Id koji je u ovoj bazi
    @PostMapping("/restartLimitUsed/{id}")
    public ResponseEntity<Actuary> restartLimitUsed(@PathVariable Long id){
        return ResponseEntity.ok(actuaryService.restartLimitUsed(id));
    }

    //postavljanje novog limita Agentu, saljemo id koji je u ovoj bazi
    //i novi limit kao parametar
    @PostMapping("/setLimit/{id}")
    public ResponseEntity<Actuary> setLimit(@PathVariable Long id, @Param("limit") Double limit){
        return ResponseEntity.ok(actuaryService.setLimit(id, limit));
    }

    //postavljamo order request na suprotnu vrednost
    @PostMapping("/setOrderRequest/{id}")
    public ResponseEntity<Actuary> setOrderRequest(@PathVariable Long id, @Param("orderRequest") String orderRequest){
        if (orderRequest.equalsIgnoreCase("true")){
            return ResponseEntity.ok(actuaryService.setOrderRequest(id, true));
        }
        if (orderRequest.equalsIgnoreCase("false")){
            return ResponseEntity.ok(actuaryService.setOrderRequest(id, false));
        }
        //TODO: pogledati ovo
        return (ResponseEntity<Actuary>) ResponseEntity.badRequest();
    }

}
