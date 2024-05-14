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
public class BuyStockUserDto {
    // OTC: User kupuje od usera
    private Long userSellerId;
    private Long userBuyerId;
    private String ticker;
    private Integer amount;
    private BigDecimal price;


}
