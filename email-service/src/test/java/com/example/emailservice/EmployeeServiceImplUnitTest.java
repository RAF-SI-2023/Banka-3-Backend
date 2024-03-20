package com.example.emailservice;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.model.EmployeeActivation;
import com.example.emailservice.repository.EmployeeActivationRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplUnitTest {

    @Mock
    private EmployeeActivationRepository employeeActivationRepository;
    @Mock
    private UserServiceClient userServiceClient;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;


    @Test
    public void employeeCreatedTest() {
        String email = "test@example.com";
        String identifier = UUID.randomUUID().toString();
        EmployeeActivation employeeActivation = new EmployeeActivation(null, email,identifier, LocalDateTime.now(), true);
        when(employeeActivationRepository.save(any())).thenReturn(employeeActivation);

        employeeServiceImpl.employeeCreated(email);

        verify(employeeActivationRepository, times(1)).save(any());
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());

        //TODO Ne znam kako da testiram unutar treda.
        verify(employeeActivationRepository, times(1)).save(any());
    }

    @Test
    public void changePasswordTest() {
        String identifier = "validIdentifier";
        String password = "newPassword";
        String email = "test@example.com";
        EmployeeActivation employeeActivation = new EmployeeActivation();
        employeeActivation.setEmail(email);

        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier))
                .thenReturn(Optional.of(employeeActivation));
        ResponseEntity<String> successfulResponse = ResponseEntity.ok("Password successfully updated");
        when(userServiceClient.setPassword(any())).thenReturn(successfulResponse);

        String result = employeeServiceImpl.changePassword(identifier, password);

        assertEquals("Password successfully changed", result);
    }
    @Test
    public void changePasswordTest_Fail_InvalidIdentifier() {
        given(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue("invalidIdentifier")).willReturn(Optional.empty());
        String identifier = "invalidIdentifier";
        String password = "newPassword";
        assertThrows(ResponseStatusException.class, () -> employeeServiceImpl.changePassword(identifier, password));
    }
    @Test
    public void changePasswordTest_Fail() {
        String identifier = "validIdentifier";
        String password = "newPassword";
        String email = "test@example.com";
        EmployeeActivation employeeActivation = new EmployeeActivation();
        employeeActivation.setEmail(email);
        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier))
                .thenReturn(Optional.of(employeeActivation));
        ResponseEntity<String> failedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password change failed");
        when(userServiceClient.setPassword(any())).thenReturn(failedResponse);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> employeeServiceImpl.changePassword(identifier, password));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("Password change failed", exception.getReason());
    }
}
