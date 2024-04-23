package com.example.bankservice.service;

import com.example.bankservice.domain.dto.creditRequest.CreditRequestCreateDto;
import com.example.bankservice.domain.dto.creditRequest.CreditRequestDto;
import com.example.bankservice.domain.dto.creditRequest.ProcessCreditRequestDto;
import com.example.bankservice.domain.mapper.CreditRequestMapper;
import com.example.bankservice.domain.model.Credit;
import com.example.bankservice.domain.model.CreditRequest;
import com.example.bankservice.domain.model.enums.CreditRequestStatus;
import com.example.bankservice.repository.CreditRepository;
import com.example.bankservice.repository.CreditRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class CreditRequestService {

    private final CreditRequestRepository creditRequestRepository;
    private final CreditRequestMapper creditRequestMapper;
    private final CreditRepository creditRepository;
    private final CreditService creditService;

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
        creditRequest.setMonthlyPaycheck(creditRequest.getAmount().divide(BigDecimal.valueOf(creditRequestCreateDto.getPaymentPeriod()), 1, RoundingMode.DOWN));

        creditRequestRepository.save(creditRequest);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void processCreditRequest(ProcessCreditRequestDto processCreditRequestDto) {
        CreditRequest creditRequest = creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId())
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        CreditRequestStatus status = null;
        if (processCreditRequestDto.getAccepted() != null) {
            status = (processCreditRequestDto.getAccepted()) ? CreditRequestStatus.ACCEPTED : CreditRequestStatus.DECLINED;
        }

        if (creditRequest.getStatus() == CreditRequestStatus.ACCEPTED) {
            throw new RuntimeException("Credit request already accepted");
        } else if (creditRequest.getStatus() == CreditRequestStatus.DECLINED) {
            throw new RuntimeException("Credit request already declined");
        }

        creditRequest.setStatus(status);
        creditRequestRepository.save(creditRequest);

        if (status == CreditRequestStatus.ACCEPTED) {
            createCredit(processCreditRequestDto, creditRequest);
        }
    }

    private void createCredit(ProcessCreditRequestDto processCreditRequestDto, CreditRequest creditRequest) {
        Credit credit = new Credit();
        credit.setUserId(creditRequest.getUserId());
        credit.setEmployeeId(processCreditRequestDto.getEmployeeId());
        credit.setName(creditRequest.getName());
        credit.setAccountNumber(creditRequest.getAccountNumber());
        credit.setAmount(creditRequest.getAmount());
        credit.setPaymentPeriod(creditRequest.getPaymentPeriod());
        credit.setFee(creditRequest.getAmount().multiply(BigDecimal.valueOf(0.1)));
        credit.setStartDate(System.currentTimeMillis());
        credit.setEndDate(calculateEndDate(credit.getStartDate(), creditRequest.getPaymentPeriod()));
        credit.setMonthlyFee((creditRequest.getAmount().multiply(new BigDecimal("1.1")))
                .divide(BigDecimal.valueOf(creditRequest.getPaymentPeriod()), 1, RoundingMode.DOWN));
        credit.setRemainingAmount(creditRequest.getAmount().multiply(new BigDecimal("1.1")));
        credit.setCurrencyMark(creditRequest.getCurrencyMark());

        creditRepository.save(credit);

        creditService.creditPayout(credit);
    }

    private long calculateEndDate(long startDate, int paymentPeriod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);

        calendar.add(Calendar.MONTH, paymentPeriod);

        return calendar.getTimeInMillis();
    }

}
