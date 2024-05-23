package rs.edu.raf.exchangeservice.domain.dto.buySell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyOptionDto {

    private Long optionId;
    private Long buyerId;
    private String stockListing; //APPL, MSFT
    private String optionType; //calls, puts
}
