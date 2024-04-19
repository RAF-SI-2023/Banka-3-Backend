package rs.edu.raf.exchangeservice.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "exchange_service_schema")
public class FutureOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long futureOrderId;
    private Long futureId;
    private Long employeeId;
    private Double price;
}