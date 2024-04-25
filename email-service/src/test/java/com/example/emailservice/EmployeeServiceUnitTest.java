//package com.example.emailservice;
//
//import com.example.emailservice.client.UserServiceClient;
//import com.example.emailservice.dto.ResetPasswordDto;
//import com.example.emailservice.domain.dto.password.TryPasswordResetDto;
//import com.example.emailservice.domain.model.EmployeeActivation;
//import com.example.emailservice.repository.EmployeeActivationRepository;
//import com.example.emailservice.service.email.EmailService;
//import com.example.emailservice.service.EmployeeService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class EmployeeServiceUnitTest {
//
//    @Mock
//    private EmployeeActivationRepository employeeActivationRepository;
//    @Mock
//    private UserServiceClient userServiceClient;
//    @Mock
//    private EmailService emailService;
//
//    @InjectMocks
//    private EmployeeService employeeService;
//
//
//    @Test
//    public void employeeCreatedTest() {
//        String email = "test@example.com";
//        String identifier = UUID.randomUUID().toString();
//        EmployeeActivation employeeActivation = new EmployeeActivation(null, email,identifier, LocalDateTime.now(), true);
//        when(employeeActivationRepository.save(any())).thenReturn(employeeActivation);
//
//        employeeService.employeeCreated(email);
//
//        verify(employeeActivationRepository, times(1)).save(any());
//        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
//
//        verify(employeeActivationRepository, times(1)).save(any());
//    }
//
//    @Test
//    public void changePasswordTest() {
//        String identifier = "validIdentifier";
//        String password = "newPassword";
//        String email = "test@example.com";
//        EmployeeActivation employeeActivation = new EmployeeActivation();
//        employeeActivation.setEmail(email);
//
//        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier))
//                .thenReturn(Optional.of(employeeActivation));
//        ResponseEntity<String> successfulResponse = ResponseEntity.ok("Password successfully updated");
//        when(userServiceClient.setEmployeePassword(any())).thenReturn(successfulResponse);
//
//        String result = employeeService.changePassword(identifier, password);
//
//        assertEquals("Password successfully changed", result);
//    }
//    @Test
//    public void changePasswordTest_Fail_InvalidIdentifier() {
//        given(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue("invalidIdentifier")).willReturn(Optional.empty());
//        String identifier = "invalidIdentifier";
//        String password = "newPassword";
//        assertThrows(Exception.class, () -> employeeService.changePassword(identifier, password));
//    }
//    @Test
//    public void changePasswordTest_Fail() {
//        String identifier = "validIdentifier";
//        String password = "newPassword";
//        String email = "test@example.com";
//        EmployeeActivation employeeActivation = new EmployeeActivation();
//        employeeActivation.setEmail(email);
//        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier))
//                .thenReturn(Optional.of(employeeActivation));
//        ResponseEntity<String> failedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password change failed");
//        when(userServiceClient.setEmployeePassword(any())).thenReturn(failedResponse);
//
//        ResponseStatusException exception =
//                assertThrows(ResponseStatusException.class, () -> employeeService.changePassword(identifier, password));
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
//        assertEquals("Password change failed", exception.getReason());
//    }
//    @Test
//    public void testResetPassword_Success() {
//        // Priprema podataka za test
//        TryPasswordResetDto tryPasswordResetDTO = new TryPasswordResetDto();
//        EmployeeActivation employeeActivation = new EmployeeActivation();
//        ResetPasswordDto resetPasswordDTO = new ResetPasswordDto();
//        ResponseEntity<String> successResponse = new ResponseEntity<>("Success", HttpStatus.OK);
//
//        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(tryPasswordResetDTO.getIdentifier())).thenReturn(Optional.of(employeeActivation));
//        when(userServiceClient.resetPassword(resetPasswordDTO)).thenReturn(successResponse);
//
//        // Izvršavanje metode koju testiramo
//        String result = employeeService.resetPassword(tryPasswordResetDTO);
//
//        // Provera rezultata
//        assertEquals("Password successfully changed", result);
//    }
//
//    @Test
//    public void testResetPassword_Failure() {
//        // Priprema podataka za test
//        TryPasswordResetDto tryPasswordResetDTO = new TryPasswordResetDto(/* Popunite polja za TryPasswordResetDTO */);
//        EmployeeActivation employeeActivation = new EmployeeActivation(/* Popunite polja za EmployeeActivation */);
//        ResetPasswordDto resetPasswordDTO = new ResetPasswordDto(/* Popunite polja za ResetPasswordDTO */);
//        ResponseEntity<String> errorResponse = new ResponseEntity<>("Error message", HttpStatus.BAD_REQUEST);
//
//        when(employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(tryPasswordResetDTO.getIdentifier())).thenReturn(Optional.of(employeeActivation));
//        when(userServiceClient.resetPassword(resetPasswordDTO)).thenReturn(errorResponse);
//
//        assertThrows(ResponseStatusException.class,() -> employeeService.resetPassword(tryPasswordResetDTO));
//    }
//
//    @Test
//    public void testTryResetPassword() throws InterruptedException {
//        // Priprema podataka za test
//        String email = "test@example.com";
//
//        employeeService.tryResetPassword(email);
//
//        verify(employeeActivationRepository, times(1)).save(any(EmployeeActivation.class));
//
//        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
//
//        Thread.sleep(1000); // Sačekajmo 1 sekundu kako bi nova nit mogla da se pokrene
//        verify(employeeActivationRepository, times(1)).save(any(EmployeeActivation.class));
//    }
//}