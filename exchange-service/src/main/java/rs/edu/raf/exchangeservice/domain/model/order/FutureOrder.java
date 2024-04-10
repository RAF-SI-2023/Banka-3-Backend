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
public class FutureOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long futureOrderId;
    private Long employeeId;
    private String contractName;
    private String status;  //PROCESSING, WAITING, FAILED, FINISHED
    private int amount;
    private int amountLeft;
    private boolean aon;
    private boolean margine;
}
