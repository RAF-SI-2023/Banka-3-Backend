package com.example.emailservice.service.impl;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.domain.dto.password.SetUserPasswordCodeDto;
import com.example.emailservice.domain.model.CompanyActivation;
import com.example.emailservice.domain.model.UserActivation;
import com.example.emailservice.repository.CompanyActivationRepository;
import com.example.emailservice.service.CompanyService;
import com.example.emailservice.service.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest  implements SchedulingConfigurer {

    @Mock
    private EmailService emailService;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private CompanyActivationRepository companyActivationRepository;

    @InjectMocks
    private CompanyService companyService;


    @Test
    void testCompanyActivation() throws InterruptedException {
        String email = "test@example.com";
        Random random = new Random();
        int code = random.nextInt(999999 - 100000 + 1) + 100000;
        CompanyActivation companyActivation = new CompanyActivation(
                null,
                email,
                code,
                LocalDateTime.now(),
                true);

        when(companyActivationRepository.save(any())).thenReturn(companyActivation);
        doNothing().when(emailService).sendSimpleMessage(eq(email), anyString(), anyString());

        companyService.companyActivation(email);

        verify(companyActivationRepository, times(1)).save(any(CompanyActivation.class));
        verify(emailService, times(1)).sendSimpleMessage(eq(email), anyString(), anyString());
    }

    @Test
    void testSetCompanyPassword_ActivationCodeFoundAndPossible() {
        SetUserPasswordCodeDto dto = new SetUserPasswordCodeDto();
        dto.setCode(123);
        dto.setEmail("test@example.com");
        dto.setPassword("testPassword");

        CompanyActivation activation = new CompanyActivation();
        activation.setActivationPossible(true);

        when(companyActivationRepository.findCompanyActivationByCodeAndActivationPossibleIsTrue(123))
                .thenReturn(Optional.of(activation));

        boolean result = companyService.setCompanyPassword(dto);

        verify(userServiceClient).setCompanyPassword(argThat(passwordDto ->
                passwordDto.getEmail().equals(dto.getEmail()) &&
                        passwordDto.getPassword().equals(dto.getPassword())
        ));

        assertTrue(result);
    }

    @Test
    void testSetCompanyPassword_ActivationCodeNotFound() {
        // Priprema testnih podataka
        SetUserPasswordCodeDto dto = new SetUserPasswordCodeDto();
        dto.setCode(123);
        dto.setEmail("test@example.com");
        dto.setPassword("testPassword");

        // Postavljanje ponašanja mock-a za repository
        when(companyActivationRepository.findCompanyActivationByCodeAndActivationPossibleIsTrue(123))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> companyService.setCompanyPassword(dto));
    }

    @Test
    void testSetCompanyPassword_ActivationCodeFoundButNotPossible() {
        SetUserPasswordCodeDto dto = new SetUserPasswordCodeDto();
        dto.setCode(123);
        dto.setEmail("test@example.com");
        dto.setPassword("testPassword");

        CompanyActivation activation = new CompanyActivation();
        activation.setActivationPossible(false);

        when(companyActivationRepository.findCompanyActivationByCodeAndActivationPossibleIsTrue(123))
                .thenReturn(Optional.of(activation));

        verify(userServiceClient, never()).setCompanyPassword(any());

        assertFalse( companyService.setCompanyPassword(dto));
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addFixedRateTask(companyService::cleanupInactiveEntities, 900000);
    }


    @Test
    void testCleanupInactiveEntities() {
        // Priprema testnih podataka
        CompanyActivation inactiveEntity = new CompanyActivation();
        inactiveEntity.setId("1L");
        inactiveEntity.setActivationPossible(false);

        // Postavljanje ponašanja mock-a za repository
        when(companyActivationRepository.findAllByActivationPossible(false))
                .thenReturn(Optional.of(Collections.singletonList(inactiveEntity)));
        // Poziv metode za čišćenje neaktivnih entiteta
        companyService.cleanupInactiveEntities();

        // Provera da li je metoda deleteAll pozvana nad repository-em
        verify(companyActivationRepository).deleteAll(Collections.singletonList(inactiveEntity));
    }

    @Test
    public void testCleanupInactiveEntities_Exception() {
        // Priprema testnih podataka

        // Mock-ovanje repozitorijuma da izbaci grešku prilikom poziva metode findAllByActivationPossible(false)
        when(companyActivationRepository.findAllByActivationPossible(false)).thenThrow(new RuntimeException("Simulated error"));

        // Poziv metode koja treba da se testira
        companyService.cleanupInactiveEntities();

        // Provera da li je greška uhvaćena i ispisana
        verify(companyActivationRepository, times(1)).findAllByActivationPossible(false);
    }
}