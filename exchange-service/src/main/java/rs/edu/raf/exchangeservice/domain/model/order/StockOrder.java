package rs.edu.raf.exchangeservice.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "exchange_service_schema")
public class StockOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockOrderId;
    private Long employeeId;
    private String ticker;
    private OrderStatus status;  //PROCESSING, WAITING, FAILED, FINISHED
    private OrderType type;    //MARKET, STOP, LIMIT, STOP-LIMIT
    private Double limitValue;
    private Double stopValue;
    private int amount;     //ukupna kolicina
    private int amountLeft;     //koliko je ostalo da se kupi
    private boolean aon;
    private boolean margin;
    private String currencyMark;
}
