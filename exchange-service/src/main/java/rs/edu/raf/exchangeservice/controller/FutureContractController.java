package rs.edu.raf.exchangeservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.exchangeservice.domain.dto.contract.ContractAnswerDto;
import rs.edu.raf.exchangeservice.service.FutureContractService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/future-contract")
public class FutureContractController {
    private final FutureContractService futureContractService;

    @GetMapping("/getAllSupervisor")
    @Operation(description = "Supervizor dohvata sve ugovore")
    public ResponseEntity<?> getAllContracts(){
        return ResponseEntity.ok(futureContractService.getAllUnresolvedContracts());
    }

    @GetMapping("/getByContractName/{contractName}")
    @Operation(description = "trazi Future ugovor po prosledjenom nazivu ugovora")
    public ResponseEntity<?> getByContractName(@PathVariable String contractName){
        return ResponseEntity.ok(futureContractService.findByFutureContractName(contractName));
    }

    @GetMapping("/getAllSent/{companyId}")
    @Operation(description = "Firma dohvata sve poslate future ugovore")
    public ResponseEntity<?> getAllSentContractsByCompanyId(@PathVariable Long companyId){
        return ResponseEntity.ok(futureContractService.getAllSentFutureContractsByCompanyId(companyId));
    }

    @GetMapping("/getAllReceived/{companyId}")
    @Operation(description = "Firma dohvata sve primljene future ugovore")
    public ResponseEntity<?> getAllReceivedContractsByCompanyId(@PathVariable Long companyId){
        return ResponseEntity.ok(futureContractService.getAllReceivedFutureContractsByCompanyId(companyId));
    }
    @PostMapping("/companyAccept")
    @Operation(description = "Kompanija prihvata future ugovor")
    public ResponseEntity<?> companyAccept(@RequestBody ContractAnswerDto dto){
        return ResponseEntity.ok(futureContractService.companyAccept(dto));
    }
    @PostMapping("/companydecline")
    @Operation(description = "Kompanija odbija future ugovor")
    public ResponseEntity<?> companyDecline(@RequestBody ContractAnswerDto dto){
        return ResponseEntity.ok(futureContractService.companyDecline(dto));
    }

    @PostMapping("/supervisorAccept")
    @Operation(description = "Supervizor prihvata future ugovor")
    public ResponseEntity<?> supervisorAccept(@RequestBody ContractAnswerDto dto){
        if(futureContractService.supervisorAccept(dto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("/supervisorDecline")
    @Operation(description = "Supervizor odbija future ugovor")
    public ResponseEntity<?> supervisorDecline(@RequestBody ContractAnswerDto dto){
        if(futureContractService.supervisorDecline(dto)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
