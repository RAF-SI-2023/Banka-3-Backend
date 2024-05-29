package com.example.bankservice.domain.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class UserOtcTransactionDto {
    private Long userFromId;
    private Long userToId;
    private Double amount;
}
