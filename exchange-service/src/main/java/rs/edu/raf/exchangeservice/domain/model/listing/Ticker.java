package rs.edu.raf.exchangeservice.domain.model.listing;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Ticker implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tickerId;

    @JsonProperty("ticker")
    private String ticker;      //oznaka npr A, IBM, APPL

    @JsonProperty("name")
    private String name;    //ime fimre

    @JsonProperty("currency_name")
    private String currencyName;    //valuta

    @JsonProperty("primary_exchange")
    private String primaryExchange;     //berza na kojoj se trejduje
}
