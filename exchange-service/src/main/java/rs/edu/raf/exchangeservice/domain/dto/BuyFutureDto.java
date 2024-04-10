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

    private Long employeeId;    //mora da stigne
    private String contractName;  //mora da stigne
    private Integer amount; //mora da stigne
    private boolean aon;
    private boolean margine;

    @Override
    public String toString() {
        return "BuyStockDto{" +
                "employeeId=" + employeeId +
                ", contractName='" + contractName + '\'' +
                ", amount=" + amount +
                ", aon=" + aon +
                ", margine=" + margine +
                '}';
    }
}
