package rs.edu.raf.userservice.domains.dto.creditrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestDto {
    private User user;
    private String name;
    private String accountNumber;
    private String currencyMark;
    private Double amount;
    private String applianceReason;
    private Double monthlyPaycheck;
    private Boolean employed;
    private Long dateOfEmployment;
    private int paymentPeriod;
    private CreditRequestStatus status;
}
