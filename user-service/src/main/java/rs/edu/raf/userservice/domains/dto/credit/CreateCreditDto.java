package rs.edu.raf.userservice.domains.dto.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCreditDto {

    private Long userId;
    private String name;
    private String currencyMark;
    private Double amount;
    private int paymentPeriod;
}
