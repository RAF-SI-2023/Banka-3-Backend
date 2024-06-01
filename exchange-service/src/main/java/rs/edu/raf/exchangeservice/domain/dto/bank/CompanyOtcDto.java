package rs.edu.raf.exchangeservice.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CompanyOtcDto {
    private Long companyFromId;
    private Long companyToId;
    private Double amount;
}
