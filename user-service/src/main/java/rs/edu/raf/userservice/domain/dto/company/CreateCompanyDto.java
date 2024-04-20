package rs.edu.raf.userservice.domain.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateCompanyDto {
    @NotBlank
    @Max(20)
    private String title;
    @NotBlank
    @Max(9)
    private String number;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Min(100000000)
    @Max(999999999)
    private int pib;
    @NotBlank
    @Min(10000000)
    @Max(99999999)
    private int maticniBroj;
    @NotBlank
    @Size(min = 4, max = 4)
    private int sifraDelatnosti;
}
