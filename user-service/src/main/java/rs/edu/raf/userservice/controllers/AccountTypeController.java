package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.services.AccountTypeService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("api/v1/account-types")
public class AccountTypeController {

    @Autowired
    private AccountTypeService accountTypeService;

    @GetMapping("/getAll")
    public List<AccountType> findAllAccountTypes() {
        return accountTypeService.findAll();
    }
}
