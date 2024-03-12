package zews.Email_Service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zews.Email_Service.entity.AccountActivation;
import zews.Email_Service.entity.AccountActivationDto;
import zews.Email_Service.service.AccountActivationService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/activeAccount", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AccountActivationController {
    private AccountActivationService accountActivationService;

    public AccountActivationController(AccountActivationService accountActivationService) {
        this.accountActivationService = accountActivationService;
    }

    @GetMapping
    public ResponseEntity<List<AccountActivation>> getAllActivations() {
        return new ResponseEntity<>(accountActivationService.allAccountActivations(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addActivationCode(@RequestBody AccountActivation addAccountActivation){
        if (accountActivationService.addAccountActivation(addAccountActivation) != null){
            return new ResponseEntity<>("Valja", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lose", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/checkCode")
    public ResponseEntity<String> checkActivationCode(@RequestBody AccountActivationDto accountActivationDto){
        if (accountActivationService.findByEmailAndCode(accountActivationDto.getEmail(),accountActivationDto.getCode())){
            //TODO: dodati da se korisnikcki nalog sa tim Emailom postaje aktivan!
            return new ResponseEntity<>("Valja", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lose", HttpStatus.BAD_REQUEST);
    }
}
