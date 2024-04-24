package rs.edu.raf.exchangeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellFutureDto {
    private Long futureId;
    private Long employeeId;
    private double price;

    @Override
    public String toString() {
        return "SellStockDto{" +
                "futureId=" + futureId +
                "employeeId=" + employeeId +
                ", price='" + price +
                '}';
    }
}
