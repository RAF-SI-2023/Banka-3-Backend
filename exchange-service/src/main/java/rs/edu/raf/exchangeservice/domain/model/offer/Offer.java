package rs.edu.raf.exchangeservice.domain.model.offer;

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
public class Offer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    private String ticker;
    private Integer amount;
    private Double price;
    private Long idBank4; //id koji ce stizati od banke
    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

}
