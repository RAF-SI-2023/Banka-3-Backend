package rs.edu.raf.userservice.e2e.byStoks.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class WithdrawFundsDto {
    private String accountNumber;
    private Double amount;
}
