package rs.edu.raf.exchangeservice.domain.dto.offer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyOfferDto implements Serializable {


    private Long myOfferId;
    private String ticker;
    private Integer amount;
    private Integer price;
}
