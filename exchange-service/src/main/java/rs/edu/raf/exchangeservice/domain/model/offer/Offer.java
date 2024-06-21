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
    private Long idBank; //id koji ce stizati od banke
    private Integer owner; //broj banke koji nam se posalo ponudu
    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;
}
