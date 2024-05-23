package rs.edu.raf.exchangeservice.domain.dto.buySell;

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
    private Long companyId; //mora da stigne

    @Override
    public String toString() {
        return "BuyStockDto{" +
                "futureId=" + futureId +
                "employeeId=" + companyId +
                '}';
    }
}
