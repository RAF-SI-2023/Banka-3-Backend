package rs.edu.raf.exchangeservice.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockOrderSell implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockOrderSellId;
    private Long employeeId;
    private String ticker;
    private String status;  //PROCESSING, FAILED, FINISHED
    private String type;    //MARKET, STOP, LIMIT, STOP-LIMIT
    private Double limitValue;
    private Double stopValue;
    private int amount;     //ukupna kolicina
    private int amountLeft;     //koliko je ostalo da se kupi
    private boolean aon;
    private boolean margine;
}
