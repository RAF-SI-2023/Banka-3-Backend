package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestCreateDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.ProcessCreditRequestDto;
import rs.edu.raf.userservice.domains.model.CreditRequest;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;
import rs.edu.raf.userservice.repositories.CreditRequestRepository;
import rs.edu.raf.userservice.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {

    @Mock
    CreditRequestRepository creditRequestRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CreditRequestService creditRequestService;

    @Test
    public void findAllTest() {
        CreditRequestDto creditRequestDto = createDummyCreditRequestDto();
        CreditRequestDto creditRequestDto2 = createDummyCreditRequestDto();

        CreditRequest creditRequest = createDummyCreditRequest();
        CreditRequest creditRequest2 = createDummyCreditRequest();

        List<CreditRequest> creditRequests = List.of(creditRequest, creditRequest2);

        given(creditRequestRepository.findAll()).willReturn(creditRequests);

        List<CreditRequestDto> result = creditRequestService.findAll();

        assertEquals(2, result.size());
        assertEquals(creditRequestDto, result.get(0));
        assertEquals(creditRequestDto2, result.get(1));

    }

    @Test
    public void findByIdTest() {
        CreditRequestDto creditRequestDto = createDummyCreditRequestDto();
        CreditRequest creditRequest = createDummyCreditRequest();

        given(creditRequestRepository.findById(1L)).willReturn(java.util.Optional.of(creditRequest));

        CreditRequestDto result = creditRequestService.findById(1L);

        assertEquals(creditRequestDto, result);
    }

    @Test
    public void createCreditRequestTest() {
        CreditRequestCreateDto creditRequestCreateDto = new CreditRequestCreateDto();
        creditRequestCreateDto.setUserId(1L);
        creditRequestCreateDto.setName("Kredit za stan");
        creditRequestCreateDto.setCurrencyMark("RSD");
        creditRequestCreateDto.setAmount(100000.0);
        creditRequestCreateDto.setPaymentPeriod(12);

        CreditRequest creditRequest = createDummyCreditRequest();
        creditRequest.setId(null);
        User user = createDummyUser("pera1234@gmail.com");
        creditRequest.setUser(user);


        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(creditRequestRepository.save(creditRequest)).willReturn(creditRequest);

        CreditRequestDto result = creditRequestService.createCreditRequest(creditRequestCreateDto);

        assertEquals(creditRequest.getName(), result.getName());
        assertEquals(creditRequest.getCurrencyMark(), result.getCurrencyMark());
        assertEquals(creditRequest.getAmount(), result.getAmount());
        assertEquals(creditRequest.getPaymentPeriod(), result.getPaymentPeriod());
        assertEquals(creditRequest.getStatus(), result.getStatus());
    }

    @Test
    public void processCreditRequestTest_Accepted() {
        CreditRequest creditRequest = createDummyCreditRequest();

        ProcessCreditRequestDto processCreditRequestDto = new ProcessCreditRequestDto();
        processCreditRequestDto.setCreditRequestId(1L);
        processCreditRequestDto.setAccepted(true);

        given(creditRequestRepository.findById(1L)).willReturn(java.util.Optional.of(creditRequest));

        given(creditRequestRepository.save(creditRequest)).willReturn(creditRequest);


        CreditRequestDto result = creditRequestService.processCreditRequest(processCreditRequestDto);

        assertEquals(creditRequest.getStatus(), result.getStatus());
    }

    @Test
    public void processCreditRequestTest_Declined() {
        CreditRequest creditRequest = createDummyCreditRequest();

        ProcessCreditRequestDto processCreditRequestDto = new ProcessCreditRequestDto();
        processCreditRequestDto.setCreditRequestId(1L);
        processCreditRequestDto.setAccepted(false);

        given(creditRequestRepository.findById(1L)).willReturn(java.util.Optional.of(creditRequest));

        given(creditRequestRepository.save(creditRequest)).willReturn(creditRequest);


        CreditRequestDto result = creditRequestService.processCreditRequest(processCreditRequestDto);

        assertEquals(creditRequest.getStatus(), result.getStatus());
    }


    private User createDummyUser(String email) {
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("Pera");
        user.setLastName("Peric");
        user.setJmbg("1234567890123");
        user.setDateOfBirth(123L);
        user.setGender("M");
        user.setPhoneNumber("+3123214254");
        user.setAddress("Mika Mikic 13");
        user.setEmail(email);
        user.setPassword("pera1234");
        user.setActive(true);
        return user;
    }

    private CreditRequestDto createDummyCreditRequestDto() {
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        User user = createDummyUser("pera123@gmail.com");
        creditRequestDto.setUser(user);
        creditRequestDto.setName("Kredit za stan");
        creditRequestDto.setCurrencyMark("RSD");
        creditRequestDto.setAmount(100000.0);
        creditRequestDto.setPaymentPeriod(12);
        creditRequestDto.setStatus(CreditRequestStatus.PROCESSING);

        return creditRequestDto;
    }

    private CreditRequest createDummyCreditRequest() {
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setId(1L);
        User user = createDummyUser("pera123@gmail.com");
        creditRequest.setUser(user);
        creditRequest.setName("Kredit za stan");
        creditRequest.setCurrencyMark("RSD");
        creditRequest.setAmount(100000.0);
        creditRequest.setPaymentPeriod(12);
        creditRequest.setStatus(CreditRequestStatus.PROCESSING);

        return creditRequest;
    }
}