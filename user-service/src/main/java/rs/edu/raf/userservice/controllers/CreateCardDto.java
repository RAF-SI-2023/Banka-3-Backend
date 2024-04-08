package rs.edu.raf.userservice.controllers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class CreateCardDto {

    @NotBlank
    @NotNull
    private Long userId;
    
    @NotBlank
    @NotNull
    private String accountNumber;

}
