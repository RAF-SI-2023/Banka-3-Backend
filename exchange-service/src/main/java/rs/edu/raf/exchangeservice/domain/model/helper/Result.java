package rs.edu.raf.exchangeservice.domain.model.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.edu.raf.exchangeservice.domain.model.listing.Ticker;

import javax.persistence.Table;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @JsonProperty("results")
    private List<Ticker> tickers;
}
