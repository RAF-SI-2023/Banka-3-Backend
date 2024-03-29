package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountCreateDto;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountDto;
import rs.edu.raf.userservice.services.ForeignAccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/foreignAccount")
public class ForeignAccountController {

    @Autowired
    private ForeignAccountService foreignAccountService;

    @GetMapping("/getAll")
    public List<ForeignAccountDto> getAll() {
        return foreignAccountService.findAll();
    }

    @GetMapping("/getByUser/{userId}")
    public List<ForeignAccountDto> getByUser(Long userId) {
        return foreignAccountService.findByUser(userId);
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public ForeignAccountDto getByAccountNumber(String accountNumber) {
        return foreignAccountService.findByAccountNumber(accountNumber);
    }

    @PostMapping("/{userId}")
    public ForeignAccountDto addUser(@PathVariable Long userId,
                                     @RequestBody ForeignAccountCreateDto foreignAccountCreateDto) {
        return foreignAccountService.create(foreignAccountCreateDto, userId);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId) {
        foreignAccountService.deactivate(accountId);
    }
}
