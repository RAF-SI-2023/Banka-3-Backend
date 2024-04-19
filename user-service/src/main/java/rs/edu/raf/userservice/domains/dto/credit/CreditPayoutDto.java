package rs.edu.raf.userservice.domains.dto.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditPayoutDto {
    private BigDecimal amount;
    private String currencyMark;
    private Long date;
}
