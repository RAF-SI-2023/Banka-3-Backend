package com.example.bankservice.domain.dto.companyaccount;


import com.example.bankservice.domain.model.Currency;
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
public class CompanyMarginAccountCreateDto {


    @NotBlank
    private Long companyId;
    @NotBlank
    private Long employeeId;
    @NotBlank
    private BigDecimal initialMargin;
    @NotBlank
    private BigDecimal maintenanceMargin;
    @NotBlank
    private Double bankParticipation;




}
