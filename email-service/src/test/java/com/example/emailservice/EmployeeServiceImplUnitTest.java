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

    /**Testovi za resetovanje rasvorda zaposlenog*/

    /**Test koji treba da prodje, testira slanje mejla na prosledjeni email zaposlenog*/
    @Test
    public void tryResetPasswordTest() throws InterruptedException {
        String email = "test@example.com";
        String identifier = UUID.randomUUID().toString();
        EmployeeActivation employeeActivation = new EmployeeActivation(null, email, identifier, LocalDateTime.now(), true);
        when(employeeActivationRepository.save(any())).thenReturn(employeeActivation);
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());

        employeeServiceImpl.tryResetPassword(email);

        verify(employeeActivationRepository, times(1)).save(any());
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
        Thread.sleep(400);
        verify(employeeActivationRepository, times(1)).save(any());
    }

    /**Test koji treba da prodje, testira prosledjivanje nove sifre zaposlenog*/
    @Test
    public void resetPasswordTest() {
        String identifier = UUID.randomUUID().toString();
        String newPassword = "newPassword";
        String email = "test@example.com";
        EmployeeActivation employeeActivation = new EmployeeActivation(null, email, identifier, LocalDateTime.now(), true);
        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier)).thenReturn(Optional.of(employeeActivation));
        ResponseEntity<String> successfulResponse = ResponseEntity.ok("Password successfully updated");
        when(userServiceClient.resetPassword(any())).thenReturn(successfulResponse);

        String result = employeeServiceImpl.resetPassword(identifier, newPassword);

        assertEquals("Password successfully changed", result);
    }

    /**Test koji failuje na exception, jer je zaposleni prosledio neodgovarajuci identifikator,
     * proveravamo da li se dobija dobar exception*/
    @Test
    public void resetPasswordTest_Fail_InvalidIdentifier() {
        String invalidIdentifier = "invalidIdentifier";
        String newPassword = "newPassword";
        given(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(invalidIdentifier))
                .willReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> employeeServiceImpl.resetPassword(invalidIdentifier, newPassword));
    }

    /**Test koji proverava ponasanje kada user service vrati neuspesan odgovor*/
    @Test
    public void resetPasswordTest_Fail_UserServiceError() {
        String identifier = UUID.randomUUID().toString();
        String newPassword = "newPassword";
        String email = "test@example.com";
        EmployeeActivation employeeActivation = new EmployeeActivation(null, email, identifier, LocalDateTime.now(), true);
        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier)).thenReturn(Optional.of(employeeActivation));
        ResponseEntity<String> failedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password change failed");
        when(userServiceClient.resetPassword(any())).thenReturn(failedResponse);

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> employeeServiceImpl.resetPassword(identifier, newPassword));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("Password change failed", exception.getReason());
    }


}
