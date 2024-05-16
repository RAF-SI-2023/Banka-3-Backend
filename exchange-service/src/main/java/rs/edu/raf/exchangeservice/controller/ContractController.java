package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.domain.model.myListing.Contract;
import rs.edu.raf.exchangeservice.service.ContractService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/contract")
public class ContractController {

    private final ContractService contractService;


    @GetMapping("/getAllSupervisor")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    @Operation(description = "Supervizor dohvata sve ugovore")
    public ResponseEntity<?> getAllContracts(){

        //TODO company accepted,supervisor proccesing
        return ResponseEntity.ok(contractService.getAllUnresolvedContracts());
    }

    @GetMapping("/getAllSent/{companyId}")
    @Operation(description = "Firma dohvata sve poslate ugovore")
    public ResponseEntity<?> getAllSentContractsByCompanyId(@PathVariable Long companyId){

        return ResponseEntity.ok(contractService.getAllSentContractsByCompanyId(companyId));
    }

    @GetMapping("/getAllReceived/{companyId}")
    @Operation(description = "Firma dohvata sve primljene ugovore")
    public ResponseEntity<?> getAllReceivedContractsByCompanyId(@PathVariable Long companyId){

        return ResponseEntity.ok(contractService.getAllReceivedContractsByCompanyId(companyId));
    }
    @GetMapping("/getAll/{companyId}")
    @Operation(description = "firma dohvata sve svoje ugovore")
    public ResponseEntity<?> getAllByCompanyId(@PathVariable Long companyId){

        return ResponseEntity.ok(contractService.getAllByCompanyId(companyId));
    }

    @PostMapping("/companyAccept")
    @Operation(description = "Kompanija prihvata ugovor")
    public ResponseEntity<?> companyAccept(@RequestBody ContractAnswerDto dto){
        return ResponseEntity.ok(contractService.companyAccept(dto));
    }

    @PostMapping("/companyDecline")
    @Operation(description = "Kompanija odbija ugovor")
    public ResponseEntity<?> companyDecline(@RequestBody ContractAnswerDto dto){

        return ResponseEntity.ok(contractService.companyDecline(dto));
    }

    @PostMapping("/supervisorAccept")
    @Operation(description = "Supervizor odobrava ugovor")
    public ResponseEntity<?> supervisorAccept(@RequestBody ContractAnswerDto dto){
        if(contractService.supervisorAccept(dto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }
    @PostMapping("/supervisorDecline")
    @Operation(description = "Supervizor odbija ugovor")
    public ResponseEntity<?> supervisorDecline(@RequestBody ContractAnswerDto dto){

        if(contractService.supervisorDecline(dto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }



    }




}
