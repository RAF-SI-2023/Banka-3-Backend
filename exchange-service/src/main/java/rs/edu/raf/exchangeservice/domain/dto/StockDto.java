package rs.edu.raf.exchangeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    private Long stockId;
    private String name;
    private String exchange;
    private Long lastRefresh;
    private String ticker;
    private double price;
    private double ask;
    private double bid;
    private double change;
    private double volume;
    private String currencyMark;
}
