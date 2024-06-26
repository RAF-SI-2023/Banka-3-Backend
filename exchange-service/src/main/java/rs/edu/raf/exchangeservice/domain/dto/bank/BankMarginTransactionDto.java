package rs.edu.raf.exchangeservice.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class BankMarginTransactionDto{
    private Long userId;
    private Long companyId;
    private Double amount;
}
