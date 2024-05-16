package rs.edu.raf.exchangeservice.domain.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractAnswerDto {

        private Long contractId;
        private String comment;


}
