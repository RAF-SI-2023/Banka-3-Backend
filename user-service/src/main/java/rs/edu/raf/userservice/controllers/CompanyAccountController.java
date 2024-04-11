package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.account.CheckEnoughBalanceDto;
import rs.edu.raf.userservice.domains.dto.account.RebalanceAccountDto;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountCreateDto;
import rs.edu.raf.userservice.domains.dto.companyaccount.CompanyAccountDto;
import rs.edu.raf.userservice.services.CompanyAccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/companyAccount")
public class CompanyAccountController {

    @Autowired
    private CompanyAccountService companyAccountService;

    @GetMapping("/getAll")
    public List<CompanyAccountDto> getAll() {
        return companyAccountService.findAll();
    }

    @GetMapping("/getByCompany/{companyId}")
    public List<CompanyAccountDto> getByCompany(Long companyId) {
        return companyAccountService.findByCompany(companyId);
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public CompanyAccountDto getByAccountNumber(String accountNumber) {
        return companyAccountService.findByAccountNumber(accountNumber);
    }

    @PostMapping("/{companyId}")
    public CompanyAccountDto addCompanyAccount(@PathVariable Long companyId,
                                               @RequestBody CompanyAccountCreateDto companyAccountDto) {
        return companyAccountService.create(companyAccountDto, companyId);
    }

    @DeleteMapping("/{companyAccountId}")
    public void deleteCompanyAccount(@PathVariable Long companyAccountId) {
        companyAccountService.deactivate(companyAccountId);
    }

    @PostMapping("/checkCompanyBalance")
    public ResponseEntity<String> checkCompanyBalance(@RequestBody CheckEnoughBalanceDto dto) {
        return companyAccountService.checkCompanyBalance(dto);
    }

    @PostMapping("/reserveCompanyMoney")
    public ResponseEntity<String> reserveCompanyMoney(@RequestBody RebalanceAccountDto dto) {
        return companyAccountService.reserveCompanyMoney(dto);
    }

    @PostMapping("/unreserveCompanyMoney")
    public ResponseEntity<String> unreserveCompanyMoney(@RequestBody RebalanceAccountDto dto) {
        return companyAccountService.unreserveCompanyMoney(dto);
    }

    @PostMapping("/addMoneyToCompanyAccount")
    public ResponseEntity<String> addMoneyToCompanyAccount(@RequestBody RebalanceAccountDto dto) {
        return companyAccountService.addMoneyToCompanyAccount(dto);
    }

    @PostMapping("/takeMoneyFromCompanyAccount")
    public ResponseEntity<String> takeMoneyFromCompanyAccount(@RequestBody RebalanceAccountDto dto) {
        return companyAccountService.takeMoneyFromCompanyAccount(dto);
    }



}
