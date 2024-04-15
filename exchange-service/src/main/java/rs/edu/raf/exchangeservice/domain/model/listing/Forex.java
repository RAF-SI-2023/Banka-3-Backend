package rs.edu.raf.exchangeservice.domain.model.listing;

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
public class Forex implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forexId;
    private String baseCurrency;
    private String quoteCurrency;
    private double conversionRate;
    private Long lastRefresh;
}
