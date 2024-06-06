package rs.edu.raf.exchangeservice.domain.model.myListing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "exchange_service_schema")
public class MyOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myOptionId;
    private String contractSymbol; //unique
    private String optionType; //calls, puts

    @JsonProperty("lastPrice")
    private double price;
    @JsonProperty("ask")
    private double ask;
    @JsonProperty("bid")
    private double bid;

    private String currencyMark;
    private int quantity;
}
