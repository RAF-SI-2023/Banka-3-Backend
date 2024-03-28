package rs.edu.raf.userservice.domains.dto.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditTransactionDto {
    private String accountFrom;
    private Long creditId;
    private Double amount;
    private String currencyMark;
    private Long date;
}
