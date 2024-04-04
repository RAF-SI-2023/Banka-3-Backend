package rs.edu.raf.exchangeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellStockDto {
    private Long employeeId;
    private String ticker;
    private Integer amount;

    @Override
    public String toString() {
        return "SellStockDto{" +
                "employeeId=" + employeeId +
                ", ticker='" + ticker + '\'' +
                ", amount=" + amount +
                '}';
    }
}
