package rs.edu.raf.userservice.domains.dto.creditrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestCreateDto {
    private Long userId;
    private String name;
    private String accountNumber;
    private Double amount;
    private String applianceReason;
    private Double monthlyPaycheck;
    private Boolean employed;
    private Long dateOfEmployment;
    private int paymentPeriod;
    private String currencyMark;
}
