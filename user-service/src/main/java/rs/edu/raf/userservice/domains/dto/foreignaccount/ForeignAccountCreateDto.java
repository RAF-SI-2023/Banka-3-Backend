package rs.edu.raf.userservice.domains.dto.foreignaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ForeignAccountCreateDto {

        private Long userId;
        private Long employeeId;
        private Double balance;
        private String currency;
        private String accountType;
}
