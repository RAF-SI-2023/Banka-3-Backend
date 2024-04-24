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
    //kada banka nesto kupuje sa BErze
    //kada banka nesto prodaje Berzi
    private Double amount;
    private String currencyMark;
}
