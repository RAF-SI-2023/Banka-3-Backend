package rs.edu.raf.userservice.domains.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import rs.edu.raf.userservice.domains.model.AccountType;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.User;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class AccountDto {
    private Long accountId;
    private String accountNumber;
    private User user;
    private Double balance;
    private Double availableBalance;
    private Long creationDate;
    private Long expireDate;
    private boolean active;
    private Employee employee;
    private Currency currency;
    private AccountType accountType;
}
