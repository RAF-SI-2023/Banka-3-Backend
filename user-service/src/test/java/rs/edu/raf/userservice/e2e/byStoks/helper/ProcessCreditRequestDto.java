package rs.edu.raf.userservice.e2e.byStoks.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProcessCreditRequestDto {
    private Long creditRequestId;
    private Long employeeId;
    private Boolean accepted;
}
