package rs.edu.raf.userservice.domains.dto.companyaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompanyAccountCreateDto {

        private Long companyId;
        private Double balance;
        private String currency;
        private String accountType;
}
