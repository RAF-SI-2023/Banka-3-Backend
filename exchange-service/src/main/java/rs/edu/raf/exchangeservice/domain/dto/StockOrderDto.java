package rs.edu.raf.exchangeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockOrderDto {

    private Long stockOrderId;
    private Long employeeId;
    private String ticker;
    private String status;  //PROCESSING, WAITING, FAILED, FINISHED
    private String type;    //MARKET, STOP, LIMIT, STOP-LIMIT
    private Double limitValue;
    private Double stopValue;
    private int amount;     //ukupna kolicina
    private int amountLeft;     //koliko je ostalo da se kupi
    private boolean aon;
    private boolean margin;
}
