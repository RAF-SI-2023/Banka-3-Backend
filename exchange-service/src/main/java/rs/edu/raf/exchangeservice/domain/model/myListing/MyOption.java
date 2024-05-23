package rs.edu.raf.exchangeservice.domain.model.myListing;

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
public class MyOption implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myOptionId;
    private Long ownerId;
    private String contractSymbol;
    private String stockListing; //povezujemo ga sa Stock
    private String optionType; //calls ili puts
    @JsonProperty("strike")
    private double strikePrice;
    @JsonProperty("impliedVolatility")
    private double impliedVolatility;
    private int openInterest;
    @JsonProperty("expiration")
    private Long settlementDate;
    private String currencyMark;
    @Version
    private Integer version;
}
