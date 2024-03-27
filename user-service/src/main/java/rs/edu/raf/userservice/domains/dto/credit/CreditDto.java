package rs.edu.raf.userservice.domains.dto.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.userservice.domains.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDto {

    private User user;
    private String name;
    private String accountNumber;
    private Double amount;
    private int paymentPeriod;
    private double fee;
    private Long startDate;
    private Long endDate;
    private Double monthlyFee;
    private Double remainingAmount;
    private String currencyMark;
}
