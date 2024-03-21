package rs.edu.raf.userservice.domains.dto.foreignaccount;

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
public class ForeignAccountDto {
    private Long foreignAccountId;
    private String accountNumber;
    private User user;
    private Employee employee;
    private Double balance;
    private Double availableBalance;
    private Long creationDate;
    private Long expireDate;
    private boolean active;
    private Currency currency;
    private AccountType accountType;
}
