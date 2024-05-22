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
public class Stock implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private String name; //iz Ticker-a, ime kompanije
    private String exchange; //iz Ticker-a, berza na kojoj se trade-uje
    private Long lastRefresh; //kada smo mi povukli podatke

    @JsonProperty("01. symbol")
    @Column(name="ticker")
    private String ticker;

    @JsonProperty("05. price")
    @Column(name="price")
    private double price;

    @JsonProperty("03. high")
    @Column(name="ask")
    private double ask;

    @JsonProperty("04. low")
    @Column(name="bid")
    private double bid;

    @JsonProperty("09. change")
    @Column(name="change")
    private double change;

    @JsonProperty("06. volume")
    @Column(name="volume")
    private double volume;

    @Column(name = "currencyMark")
    private String currencyMark;

    private boolean isPublic; //Ukoliko je public,druge firme mogu da ga kupuju


}
