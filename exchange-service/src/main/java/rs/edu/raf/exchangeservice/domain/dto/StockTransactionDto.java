package rs.edu.raf.exchangeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class StockTransactionDto {
    private String accountFrom;
    private String accountTo;
    private Double amount;
    private String currencyMark;
}
