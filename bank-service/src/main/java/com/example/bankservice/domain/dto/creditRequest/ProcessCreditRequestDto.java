package com.example.bankservice.domain.dto.creditRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProcessCreditRequestDto {
    private Long creditRequestId;
    private Boolean accepted;
}
