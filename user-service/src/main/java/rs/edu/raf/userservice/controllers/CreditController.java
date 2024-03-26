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

    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CreditDto> getAllCredits() {
        return creditService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditDto getCreditById(@PathVariable Long id) {
        return creditService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditDto createCredit(@RequestBody CreateCreditDto createCreditDto) {
        return creditService.createCredit(createCreditDto);
    }

}
