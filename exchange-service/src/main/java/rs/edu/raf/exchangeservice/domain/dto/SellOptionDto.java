package rs.edu.raf.exchangeservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellOptionDto {

    private Long employeeId;
    private String ticker;
    private Double price;
    private Double limitValue;
    private Double stopValue;
    private boolean margine;

    @Override
    public String toString() {
        return "SellStockDto{" +
                "employeeId=" + employeeId +
                ", ticker='" + ticker +
                ", price=" + price +
                ", limitValue=" + limitValue +
                ", stopValue=" + stopValue +
                ", margine=" + margine +
                '}';
    }

}
