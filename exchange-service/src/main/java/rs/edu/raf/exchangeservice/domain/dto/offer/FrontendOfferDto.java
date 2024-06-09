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
public class FrontendOfferDto implements Serializable {

    private String ticker;
    private Integer amount;
    private Double price;
}
