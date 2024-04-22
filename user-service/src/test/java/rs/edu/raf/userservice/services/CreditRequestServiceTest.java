package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.model.User;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {

//    @Mock
//    CreditRequestRepository creditRequestRepository;
//
//    @Mock
//    UserRepository userRepository;
//    @Mock
//    CreditService creditService;
//    @InjectMocks
//    CreditRequestService creditRequestService;
//
//        @Test
//        public void findAllTest() {
//
//            CreditRequest creditRequest1 = createDummyCreditRequest();
//            CreditRequest creditRequest2 = createDummyCreditRequest();
//
//            List<CreditRequest> creditRequests = List.of(creditRequest1, creditRequest2);
//
//            when(creditRequestRepository.findAll()).thenReturn(creditRequests);
//
//            List<CreditRequestDto> result = creditRequestService.findAll();
//
//            List<CreditRequestDto> expected = creditRequests.stream()
//                    .map(CreditRequestMapper.INSTANCE::creditRequestToCreditRequestDto)
//                    .collect(Collectors.toList());
//
//            assertEquals(expected.size(), result.size());
//            for (int i = 0; i < expected.size(); i++) {
//                assertEquals(expected.get(i), result.get(i));
//            }
//        }
//
//    @Test
//    public void findByIdTest() {
//        CreditRequest creditRequest = createDummyCreditRequest();
//
//        when(creditRequestRepository.findById(any(Long.class))).thenReturn(Optional.of(creditRequest));
//
//        CreditRequestDto result = creditRequestService.findById(1L);
//
//        CreditRequestDto expected = CreditRequestMapper.INSTANCE.creditRequestToCreditRequestDto(creditRequest);
//
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testCreateCreditRequest() {
//        // Given
//        CreditRequestCreateDto createDto = new CreditRequestCreateDto();
//        createDto.setUserId(1L);
//
//        User user = createDummyUser("pera@gmail.com");
//
//
//        CreditRequest creditRequest = createDummyCreditRequest();
//
//        // Mock-ovanje ponašanja repozitorijuma
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(creditRequest);
//
//        // When
//        CreditRequestDto resultDto = creditRequestService.createCreditRequest(createDto);
//
//        // Then
//        assertNotNull(resultDto);
//        assertEquals(1L, resultDto.getUser().getUserId());
//    }
//
//@Test
//public void testProcessCreditRequest_Accepted() {
//    // Given
//    ProcessCreditRequestDto requestDto = new ProcessCreditRequestDto();
//    requestDto.setCreditRequestId(1L);
//    requestDto.setAccepted(true);
//
//    CreditRequest creditRequest = createDummyCreditRequest();
//
//    when(creditRequestRepository.findById(1L)).thenReturn(java.util.Optional.of(creditRequest));
//
//    creditRequestService.processCreditRequest(requestDto);
//
//    verify(creditRequestRepository, times(2)).findById(1L);
//    verify(creditService, times(1)).createCredit(any(CreateCreditDto.class));
//    assertEquals(CreditRequestStatus.ACCEPTED, creditRequest.getStatus());
//}
//
//
//    private User createDummyUser(String email) {
//        User user = new User();
//        user.setUserId(1L);
//        user.setFirstName("Pera");
//        user.setLastName("Peric");
//        user.setJmbg("1234567890123");
//        user.setDateOfBirth(123L);
//        user.setGender("M");
//        user.setPhoneNumber("+3123214254");
//        user.setAddress("Mika Mikic 13");
//        user.setEmail(email);
//        user.setPassword("pera1234");
//        user.setActive(true);
//        return user;
//    }
//
//    private CreditRequestDto createDummyCreditRequestDto() {
//        CreditRequestDto creditRequestDto = new CreditRequestDto();
//        User user = createDummyUser("pera123@gmail.com");
//        creditRequestDto.setUser(user);
//        creditRequestDto.setName("Kredit za stan");
//        creditRequestDto.setCurrencyMark("RSD");
//        creditRequestDto.setAmount(100000.0);
//        creditRequestDto.setPaymentPeriod(12);
//        creditRequestDto.setStatus(CreditRequestStatus.PROCESSING);
//
//        return creditRequestDto;
//    }
//
//    private CreditRequest createDummyCreditRequest() {
//        CreditRequest creditRequest = new CreditRequest();
//        creditRequest.setCreditRequestId(1L);
//        User user = createDummyUser("pera123@gmail.com");
//        creditRequest.setUser(user);
//        creditRequest.setName("Kredit za stan");
//        creditRequest.setCurrencyMark("RSD");
//        creditRequest.setAmount(100000.0);
//        creditRequest.setPaymentPeriod(12);
//        creditRequest.setStatus(CreditRequestStatus.PROCESSING);
//
//        return creditRequest;
//    }
}