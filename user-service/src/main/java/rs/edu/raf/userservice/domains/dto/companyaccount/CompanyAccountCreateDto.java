package rs.edu.raf.userservice.domains.dto.companyaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompanyAccountCreateDto {
    private Long companyId;
    private BigDecimal balance;
    private String currency;
    private String accountType;
    private Long employeeId;
}
