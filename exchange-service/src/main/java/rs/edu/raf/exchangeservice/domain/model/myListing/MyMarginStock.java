package rs.edu.raf.exchangeservice.domain.model.myListing;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MyMarginStock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myMarginStockId;
    private String ticker;
    private Long userId;
    private Long companyId;
    private Integer amount;
    private String currencyMark;
    @JsonIgnore
    private Double minimumPrice;
    @Version
    private Integer version;
}
