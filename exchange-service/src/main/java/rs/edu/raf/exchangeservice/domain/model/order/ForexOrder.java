package rs.edu.raf.exchangeservice.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.exchangeservice.domain.model.enums.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "exchange_service_schema")
public class ForexOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forexOrderId;
    private Long companyId;
    private String quoteCurrency; //valuta koju sam kupio
    private Double amount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status; //PROCESSING, WAITING, FAILED, FINISHED
}
