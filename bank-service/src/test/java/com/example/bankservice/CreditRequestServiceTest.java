package com.example.bankservice;

import com.example.bankservice.domain.dto.creditRequest.CreditRequestCreateDto;
import com.example.bankservice.domain.dto.creditRequest.CreditRequestDto;
import com.example.bankservice.domain.dto.creditRequest.ProcessCreditRequestDto;
import com.example.bankservice.domain.mapper.CreditRequestMapper;
import com.example.bankservice.domain.model.CreditRequest;
import com.example.bankservice.domain.model.enums.CreditRequestStatus;
import com.example.bankservice.repository.CreditRepository;
import com.example.bankservice.repository.CreditRequestRepository;
import com.example.bankservice.service.CreditRequestService;
import com.example.bankservice.service.CreditService;
import io.cucumber.java.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {
    @Mock
    private CreditRequestRepository creditRequestRepository;

    @Mock
    private CreditRepository creditRepository;
    @Mock
    private CreditRequestMapper creditRequestMapper;
    @Mock
    private CreditService creditService;
    @InjectMocks
    private CreditRequestService creditRequestService;

    @Before
    public void setUp() {
        // Inicijalizacija mock objekata
        creditService = mock(CreditService.class);
        creditRequestRepository = mock(CreditRequestRepository.class);
        creditRepository = mock(CreditRepository.class);
        creditRequestService = new CreditRequestService(creditRequestRepository, creditRequestMapper, creditRepository, creditService);
    }

    @Test
    public void testFindAll() {
        CreditRequest creditRequest1 = new CreditRequest();
        CreditRequest creditRequest2 = new CreditRequest();
        List<CreditRequest> mockCreditRequests = new ArrayList<>();
        mockCreditRequests.add(creditRequest1);
        mockCreditRequests.add(creditRequest2);

        CreditRequestDto creditRequestDto1 = new CreditRequestDto();
        CreditRequestDto creditRequestDto2 = new CreditRequestDto();
        List<CreditRequestDto> expectedCreditRequestDtos = new ArrayList<>();
        expectedCreditRequestDtos.add(creditRequestDto1);
        expectedCreditRequestDtos.add(creditRequestDto2);

        when(creditRequestRepository.findAll()).thenReturn(mockCreditRequests);

        when(creditRequestMapper.creditRequestToCreditRequestDto(creditRequest1)).thenReturn(creditRequestDto1);
        when(creditRequestMapper.creditRequestToCreditRequestDto(creditRequest2)).thenReturn(creditRequestDto2);

        List<CreditRequestDto> actualCreditRequestDtos = creditRequestService.findAll();

        assertEquals(expectedCreditRequestDtos, actualCreditRequestDtos);
    }

    @Test
    public void testFindById_ExistingId() {
        Long id = 123L;
        CreditRequest creditRequest = new CreditRequest(/* populate credit request fields */);
        CreditRequestDto expectedCreditRequestDto = new CreditRequestDto(/* populate credit request DTO fields */);

        when(creditRequestRepository.findById(id)).thenReturn(Optional.of(creditRequest));

        when(creditRequestMapper.creditRequestToCreditRequestDto(creditRequest)).thenReturn(expectedCreditRequestDto);

        CreditRequestDto actualCreditRequestDto = creditRequestService.findById(id);

        assertEquals(expectedCreditRequestDto, actualCreditRequestDto);
    }

    @Test
    public void testFindById_NonExistingId() {
        Long id = 123L;

        when(creditRequestRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> creditRequestService.findById(id), "Credit request not found");
    }

    @Test
    public void testCreateCreditRequest() {
        CreditRequestCreateDto creditRequestCreateDto = new CreditRequestCreateDto();
        creditRequestCreateDto.setAmount(1000.0);
        creditRequestCreateDto.setPaymentPeriod(12);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setAmount(BigDecimal.valueOf(1000));
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);
        creditRequest.setMonthlyPaycheck(BigDecimal.valueOf(83.33));

        when(creditRequestMapper.creditRequestCreateDtoToCreditRequest(creditRequestCreateDto)).thenReturn(creditRequest);

        creditRequestService.createCreditRequest(creditRequestCreateDto);

        verify(creditRequestRepository).save(creditRequest);
    }
    @Test
    public void testProcessCreditRequest_CreditRequestAccepted() {
        ProcessCreditRequestDto processCreditRequestDto = new ProcessCreditRequestDto();
        processCreditRequestDto.setCreditRequestId(123L);
        processCreditRequestDto.setAccepted(true);


        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);
        creditRequest.setCreditRequestId(1L);
        creditRequest.setUserId(1L);
        creditRequest.setName("Name");
        creditRequest.setAccountNumber("accountNumber");
        creditRequest.setAmount(BigDecimal.valueOf(100));
        creditRequest.setApplianceReason("ApplianceReason");
        creditRequest.setMonthlyPaycheck(BigDecimal.valueOf(20));
        creditRequest.setEmployed(true);
        creditRequest.setDateOfEmployment(100L);
        creditRequest.setPaymentPeriod(4);
        creditRequest.setCurrencyMark("USD");


        when(creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId())).thenReturn(Optional.of(creditRequest));

        creditRequestService.processCreditRequest(processCreditRequestDto);

        assertEquals(CreditRequestStatus.ACCEPTED, creditRequest.getStatus());
        verify(creditRequestRepository).save(creditRequest);
    }

    @Test
    public void testProcessCreditRequest_CreditRequestAlreadyAccepted() {
        // Mocking data
        ProcessCreditRequestDto processCreditRequestDto = new ProcessCreditRequestDto();
        processCreditRequestDto.setCreditRequestId(123L);
        processCreditRequestDto.setAccepted(true);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setStatus(CreditRequestStatus.ACCEPTED);

        // Mocking behavior of creditRequestRepository
        when(creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId())).thenReturn(Optional.of(creditRequest));

        // Executing the method and asserting the exception
        assertThrows(RuntimeException.class, () -> creditRequestService.processCreditRequest(processCreditRequestDto), "Credit request already accepted");
    }
    @Test
    public void testProcessCreditRequest_CreditRequestAlreadyDeclined() {
        // Mocking data
        ProcessCreditRequestDto processCreditRequestDto = new ProcessCreditRequestDto();
        processCreditRequestDto.setCreditRequestId(123L);
        processCreditRequestDto.setAccepted(true);

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setStatus(CreditRequestStatus.DECLINED);

        // Mocking behavior of creditRequestRepository
        when(creditRequestRepository.findById(processCreditRequestDto.getCreditRequestId())).thenReturn(Optional.of(creditRequest));

        // Executing the method and asserting the exception
        assertThrows(RuntimeException.class, () -> creditRequestService.processCreditRequest(processCreditRequestDto), "Credit request already accepted");
    }
}