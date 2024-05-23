package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionTransactionDto {

    private Long companyAccountId;
    private Double optionPrice;
}
