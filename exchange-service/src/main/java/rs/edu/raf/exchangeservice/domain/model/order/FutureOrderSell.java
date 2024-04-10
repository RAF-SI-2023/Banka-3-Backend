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
public class FutureOrderSell implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long futureOrderSellId;
    private Long employeeId;
    private String contractName;
    private String status;  //PROCESSING, FAILED, FINISHED
    private int amount;     //ukupna kolicina
    private int amountLeft;     //koliko je ostalo da se kupi
    private boolean aon;
    private boolean margine;
}