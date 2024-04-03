package rs.edu.raf.exchangeservice.domain.model.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.edu.raf.exchangeservice.domain.model.Option;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallsOptions {
    private List<Option> options;
}
