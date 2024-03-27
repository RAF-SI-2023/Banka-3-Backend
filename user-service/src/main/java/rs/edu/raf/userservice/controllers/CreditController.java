package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.services.CreditService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/credit")
public class CreditController {

    private CreditService creditService;

    /**
     * Dohvatanje svih kredita za korisnika ciji userId je prosledjen
     * @param userId
     * @return List<CreditDto>
     */
    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CreditDto> getUserCredits(@PathVariable Long userId) {
        return creditService.findAllUserCredits(userId);
    }

    /**
     * Dohvatanje svih kredita iz baze. Endpoint predvidjen samo za zaposlenih sa
     * rolom CREDIT_OFFICER
     * @return List<CreditDto>
     */
    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CreditDto> getAllCredits() {
        return creditService.findAll();
    }

    /**
     * Kreiranje kredita nakon odobravnja kredita (PUT "/credit-request" endpoint). Nakon kreiranja
     * kredita, na racun korisnika se uplacuje iznos kredita, a kredit se otplacuje na mesecnom nivou
     * (koristeci schedule metodu unutar credit service-a).
     * @param createCreditDto
     * @return CreditDto
     */
    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditDto createCredit(@RequestBody CreateCreditDto createCreditDto) {
        return creditService.createCredit(createCreditDto);
    }

}
