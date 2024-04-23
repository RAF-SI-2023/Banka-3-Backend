package com.example.bankservice.service;

import com.example.bankservice.domain.dto.creditRequest.CreditRequestCreateDto;
import com.example.bankservice.domain.dto.creditRequest.CreditRequestDto;
import com.example.bankservice.domain.dto.creditRequest.ProcessCreditRequestDto;
import com.example.bankservice.domain.mapper.CreditRequestMapper;
import com.example.bankservice.domain.model.CreditRequest;
import com.example.bankservice.domain.model.enums.CreditRequestStatus;
import com.example.bankservice.repository.CreditRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class CreditRequestService {

    private final CreditRequestRepository creditRequestRepository;
    private final CreditRequestMapper creditRequestMapper;

    public List<CreditRequestDto> findAll() {
        return creditRequestRepository.findAll().stream()
                .map(creditRequestMapper::creditRequestToCreditRequestDto)
                .toList();
    }

    public CreditRequestDto findById(Long id) {
        CreditRequest creditRequest = creditRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Credit request not found"));
        return creditRequestMapper.creditRequestToCreditRequestDto(creditRequest);
    }

    public void createCreditRequest(CreditRequestCreateDto creditRequestCreateDto) {
        CreditRequest creditRequest = creditRequestMapper.creditRequestCreateDtoToCreditRequest(creditRequestCreateDto);
        creditRequest.setAmount(BigDecimal.valueOf(creditRequestCreateDto.getAmount()));
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);
        creditRequest.setMonthlyPaycheck(creditRequest.getAmount().divide(BigDecimal.valueOf(creditRequestCreateDto.getPaymentPeriod()),1, RoundingMode.DOWN));

        creditRequestRepository.save(creditRequest);
    }

    public void processCreditRequest(ProcessCreditRequestDto processCreditRequestDto) {
        CreditRequest creditRequest = creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId())
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        CreditRequestStatus status = null;
        if (processCreditRequestDto.getAccepted() != null) {
           status = (processCreditRequestDto.getAccepted()) ? CreditRequestStatus.ACCEPTED : CreditRequestStatus.DECLINED;
        }
        creditRequest.setStatus(status);

        creditRequestRepository.save(creditRequest);
    }
}
