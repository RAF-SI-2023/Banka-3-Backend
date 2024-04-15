package rs.edu.raf.exchangeservice.domain.model.myListing;

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
public class MyStock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myStockId;
    private String ticker;
    private Integer amount;
    @Version
    private Integer version;
}
