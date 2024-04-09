package rs.edu.raf.userservice.domains.dto.companyaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import rs.edu.raf.userservice.domains.model.Company;
import rs.edu.raf.userservice.domains.model.Currency;
import rs.edu.raf.userservice.domains.model.Employee;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyAccountDto {
    private Long companyAccountId;
    private Company company;
    private Employee employee;
    private String accountNumber;
    private Double balance;
    private Double availableBalance;
    private Long creationDate;
    private Long expireDate;
    private boolean active;
    private Currency currency;

}
