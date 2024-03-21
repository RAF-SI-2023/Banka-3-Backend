package rs.edu.raf.userservice.domains.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountCreateDto {

    @NotBlank
    private Long employeeId;
    @NotBlank
    private Long userId;
    @NotBlank
    private Double balance;
    @NotBlank
    private String currency;
    @NotBlank
    private String accountType;
}
