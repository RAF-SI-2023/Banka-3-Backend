package rs.edu.raf.userservice.e2e.byStoks.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyStockDto {
    private Long employeeId;    //mora da stigne
    private String ticker;  //mora da stigne
    private Integer amount; //mora da stigne
    private Double limitValue;
    private Double stopValue;
    private boolean aon;
    private boolean margin;

    @Override
    public String toString() {
        return "{" +
                "employeeId=" + employeeId +
                ", ticker='" + ticker + '\'' +
                ", amount=" + amount +
                ", limitValue=" + limitValue +
                ", stopValue=" + stopValue +
                ", aon=" + aon +
                ", margine=" + margin +
                '}';
    }
}
