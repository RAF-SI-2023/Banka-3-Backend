package rs.edu.raf.exchangeservice.domain.model.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.edu.raf.exchangeservice.domain.model.Stock;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalQuote {
    @JsonProperty("Global Quote")
    private Stock stock;
}
