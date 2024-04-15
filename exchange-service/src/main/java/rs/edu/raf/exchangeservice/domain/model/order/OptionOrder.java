package rs.edu.raf.exchangeservice.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.exchangeservice.domain.model.enums.StockOrderType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionOrderId;
    private Long employeeId;
    private Double price;
    private String symbol;
    private String ticker;
    private String status;  //PROCESSING, FAILED, FINISHED
    private String type;    //MARKET, STOP, LIMIT, STOP-LIMIT
    private Double limitValue;
    private Double stopValue;
    private boolean margin;
}
