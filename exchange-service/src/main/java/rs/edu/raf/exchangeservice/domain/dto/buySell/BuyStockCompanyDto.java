package rs.edu.raf.exchangeservice.domain.dto.buySell;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyStockCompanyDto {
    //Firma kupuje od firme
    private Long sellerId;
    private Long buyerId;
    private String ticker;
    private Integer amount;
    private BigDecimal price;
}
