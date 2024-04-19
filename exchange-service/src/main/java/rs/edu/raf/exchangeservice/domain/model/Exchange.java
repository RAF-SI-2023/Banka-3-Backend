package rs.edu.raf.exchangeservice.domain.model;

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
public class Exchange implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exchangeId;
    private String exchangeName;    //puno ime berze
    private String exchangeAcronym; //neki akronim, nzm sta je
    private String exchange;    //skracenica berze koja ga povezuje sa Stock
    private String country;
    private String currency;
    private String timeZone;
    private Long openTime;
    private Long closeTime;
}
