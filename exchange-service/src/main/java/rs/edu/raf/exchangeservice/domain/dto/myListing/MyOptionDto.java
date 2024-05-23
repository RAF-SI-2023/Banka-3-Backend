package rs.edu.raf.exchangeservice.domain.dto.myListing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyOptionDto {
    private Long myOptionId;
    private Long ownerId;
    private String contractSymbol;
    private String stockListing; //povezujemo ga sa Stock
    private String optionType; //calls ili puts
    private double strikePrice;
    private double impliedVolatility;
    private int openInterest;
    private Long settlementDate;
    private String currencyMark;
}
