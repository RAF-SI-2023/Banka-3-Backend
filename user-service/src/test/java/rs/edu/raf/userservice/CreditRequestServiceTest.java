package rs.edu.raf.userservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestCreateDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestDto;
import rs.edu.raf.userservice.domains.mappers.CreditRequestMapper;
import rs.edu.raf.userservice.domains.model.CreditRequest;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.CreditRequestStatus;
import rs.edu.raf.userservice.repositories.CreditRequestRepository;
import rs.edu.raf.userservice.repositories.UserRepository;
import rs.edu.raf.userservice.services.CreditRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {

    @Mock
    private CreditRequestRepository creditRequestRepository;

    @InjectMocks
    private CreditRequestService creditRequestService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindAll() {
        List<CreditRequest> creditRequests = new ArrayList<>();
        creditRequests.add(new CreditRequest());
        when(creditRequestRepository.findAll()).thenReturn(creditRequests);

        List<CreditRequestDto> result = creditRequestService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        CreditRequest creditRequest = new CreditRequest();
        when(creditRequestRepository.findById(1L)).thenReturn(Optional.of(creditRequest));

        CreditRequestDto result = creditRequestService.findById(1L);

        assertNotNull(result);
    }

    @Test
    void testCreateCreditRequest() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        CreditRequestCreateDto creditRequestCreateDto = new CreditRequestCreateDto();
        creditRequestCreateDto.setUserId(1L);
        CreditRequest creditRequest = CreditRequestMapper.INSTANCE.creditRequestCreateDtoToCreditRequest(creditRequestCreateDto);

        when(creditRequestRepository.save(any())).thenReturn(creditRequest);

        CreditRequestDto result = creditRequestService.createCreditRequest(creditRequestCreateDto);

        assertNotNull(result);
        assertEquals(CreditRequestStatus.PROCESSING, result.getStatus());
    }

    @Test
    void testCreateCreditRequest_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            creditRequestService.createCreditRequest(new CreditRequestCreateDto());
        });
    }


}