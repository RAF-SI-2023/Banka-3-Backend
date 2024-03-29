package rs.edu.raf.userservice.domains.dto.creditrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessCreditRequestDto {
    private Long creditRequestId;
    private Boolean accepted;
}
