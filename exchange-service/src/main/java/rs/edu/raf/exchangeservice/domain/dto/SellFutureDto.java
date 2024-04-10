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

    private Long employeeId;
    private String contractName;
    private Integer amount;
    private boolean aon;
    private boolean margine;

    @Override
    public String toString() {
        return "SellStockDto{" +
                "employeeId=" + employeeId +
                ", contractName='" + contractName + '\'' +
                ", amount=" + amount +
                ", aon=" + aon +
                ", margine=" + margine +
                '}';
    }
}
