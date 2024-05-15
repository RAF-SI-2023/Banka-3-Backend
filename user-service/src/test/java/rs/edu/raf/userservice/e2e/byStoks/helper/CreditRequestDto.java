package rs.edu.raf.userservice.e2e.byStoks.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreditRequestDto {
    private Long userId;
    private Long creditRequestId;
    private String name;
    private String accountNumber;
    private String currencyMark;
    private Double amount;
    private String applianceReason;
    private Double monthlyPaycheck;
    private Boolean employed;
    private Long dateOfEmployment;
    private int paymentPeriod;
    private String status;
}
