package rs.edu.raf.userservice.domains.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class RebalanceAccountDto {
    private String accountNumber;
    private Double amount;
    private String currencyMark;
}
