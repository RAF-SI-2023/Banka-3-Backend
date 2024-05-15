package rs.edu.raf.userservice.e2e.byStoks.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyAccountDto {
    private Long accountId;
    private Long companyId;
    private Long employeeId;
    private String accountNumber;
    private BigDecimal reservedAmount;
    private BigDecimal availableBalance;
    private Long creationDate;
    private Long expireDate;
    private boolean active;
    private Currency currency;

}

