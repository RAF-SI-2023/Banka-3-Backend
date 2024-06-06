package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuyOptionDto {

    private String contractSymbol; //jedinstven
    private String optionType; // calls ili puts
    private int amount;
}
