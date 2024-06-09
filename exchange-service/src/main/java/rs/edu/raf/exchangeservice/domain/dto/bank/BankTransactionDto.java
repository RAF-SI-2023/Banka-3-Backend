package rs.edu.raf.exchangeservice.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class BankTransactionDto {
    //kada banka nesto kupuje sa BErze
    //kada banka nesto prodaje Berzi
    private Long userId;
    private Long companyId;
    private Long employeeId;
    private Double amount;
    private String currencyMark;
    private double tax;
}
