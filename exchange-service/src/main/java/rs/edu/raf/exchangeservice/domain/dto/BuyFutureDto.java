package rs.edu.raf.exchangeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyFutureDto {

    private Long futureId;    //mora da stigne
    private Long employeeId; //mora da stigne
    private double price;  //mora da stigne

    @Override
    public String toString() {
        return "BuyStockDto{" +
                "futureId=" + futureId +
                "employeeId=" + employeeId +
                ", price=" + price +
                '}';
    }
}
