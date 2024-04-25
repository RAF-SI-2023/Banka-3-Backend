package rs.edu.raf.exchangeservice.controller;

import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.ActuaryDto;
import rs.edu.raf.exchangeservice.service.ActuaryService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/actuary")
public class ActuaryController {
    private final ActuaryService actuaryService;

    @GetMapping("/getAll")
    @Operation(description = "dohvatamo sve zaposlene sa ROLE_AGENT")
    public ResponseEntity<?> getAllEmployees(){
        return ResponseEntity.ok(actuaryService.findAllAgents());
    }

    @GetMapping("/restartLimitUsed/{id}")
    @Operation(description = "kada hocemo da zaposlenom restartujemo Limit, posaljemo njegov employeeId")
    public ResponseEntity<?> restartLimitUsed(@PathVariable Long id){
        return ResponseEntity.ok(actuaryService.restartLimitUsed(id));
    }

    @GetMapping("/setLimit/{id}")
    @Operation(description = "setovanje novog limita zaposlenom, salje se actuaryId, a novi limit je parametar")
    public ResponseEntity<?> setLimit(@PathVariable Long id, @Param("limit") Double limit){
        return ResponseEntity.ok(actuaryService.setLimit(id, limit));
    }

    @PostMapping("/setOrderRequest/{id}")
    @Operation(description = "postavljamo order request na zeljenu vrednost")
    public ResponseEntity<?> setOrderRequest(@PathVariable Long id, @Param("orderRequest") String orderRequest){
        if (orderRequest.equalsIgnoreCase("true")){
            return ResponseEntity.ok(actuaryService.setOrderRequest(id, true));
        }
        if (orderRequest.equalsIgnoreCase("false")){
            return ResponseEntity.ok(actuaryService.setOrderRequest(id, false));
        }
        return ResponseEntity.badRequest().body("nije dobar boolean");
    }

    @PostMapping("/addActuary")
    @Operation(description = "kada se u User Servicu doda zaposleni sa nekom rolom koja treba Exchange servicu-u, ovu rutu ce da gadja kako bi se napravio novi aktura")
    public ResponseEntity<?> addActuary(@RequestBody ActuaryDto actuaryDto){
        actuaryService.addActuary(actuaryDto);
        return ResponseEntity.ok().build();
    }
}
