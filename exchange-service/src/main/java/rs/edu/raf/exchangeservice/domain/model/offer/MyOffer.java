package rs.edu.raf.exchangeservice.domain.model.offer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(schema = "exchange_service_schema")
public class MyOffer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myOfferId;

    private String ticker;
    private Integer amount;
    private Double price;

    private OfferStatus offerStatus;
}
