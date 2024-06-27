package com.example.bankservice.domain.dto.account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class UserMarginAccountCreateDto {

    @NotBlank
    private Long employeeId;
    @NotBlank
    private Long userId;
    @NotBlank
    private BigDecimal initialMargin;
    @NotBlank
    private BigDecimal maintenanceMargin;
    @NotBlank
    private Double bankParticipation;





}
