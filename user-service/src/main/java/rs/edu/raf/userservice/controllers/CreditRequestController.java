package rs.edu.raf.userservice.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestCreateDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.ProcessCreditRequestDto;
import rs.edu.raf.userservice.services.CreditRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/credit-request")
public class CreditRequestController {

    private CreditRequestService creditRequestService;

    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CreditRequestDto> getAllCredits() {
        return creditRequestService.findAll();
    }

    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditRequestDto getCreditById(@PathVariable Long id) {
        return creditRequestService.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CreditRequestDto createCreditRequest(@RequestBody CreditRequestCreateDto creditRequestCreateDto) {
        return creditRequestService.createCreditRequest(creditRequestCreateDto);
    }

    /**
     * Zaposleni koji ima role CREDIT_OFFICER odobrava ili odbacuje zahtev za kredit na ovom endpoint-u.
     * (NAPOMENA - metoda samo azurira objekat CreditRequest, tj. njegov status, ne pravi Credit. Za pravljenje
     * kredita pogledati endpoint "api/v1/credit")
     *
     * @param processCreditRequestDto
     * @return creditRequestDto
     */
    @PreAuthorize("hasRole('ROLE_CREDIT_OFFICER')")
    @PutMapping
    public CreditRequestDto processCreditRequest(@RequestBody ProcessCreditRequestDto processCreditRequestDto) {
        return creditRequestService.processCreditRequest(processCreditRequestDto);
    }
}
