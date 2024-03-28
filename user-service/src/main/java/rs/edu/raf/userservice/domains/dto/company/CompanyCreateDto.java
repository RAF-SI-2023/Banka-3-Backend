package rs.edu.raf.userservice.domains.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompanyCreateDto {
    @NotBlank
    private String title;
    @NotBlank
    private String number;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Min(100000000)
    @Max(999999999)
    private int pib; //unique
    @NotBlank
    @Min(10000000)
    @Max(99999999)
    private int maticniBroj; //unique
    @NotBlank
    @Size(min = 1000, max = 9000)
    private int sifraDelatnosti;
}
