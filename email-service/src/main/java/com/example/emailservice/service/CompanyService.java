package com.example.emailservice.service;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.domain.dto.password.SetPasswordDto;
import com.example.emailservice.domain.dto.password.SetUserPasswordCodeDto;
import com.example.emailservice.domain.model.CompanyActivation;
import com.example.emailservice.repository.CompanyActivationRepository;
import com.example.emailservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final EmailService emailService;
    private final UserServiceClient userServiceClient;
    private final CompanyActivationRepository companyActivationRepository;

    //generise sestocifreni kod koji se salje na mail
    public void companyActivation(String email) {
        Integer code = new Random().nextInt(100000, 999999);
        CompanyActivation companyActivation = new CompanyActivation(null,
                email,
                code,
                LocalDateTime.now(),
                true);
        companyActivationRepository.save(companyActivation);
        emailService.sendSimpleMessage(email, getSubject(), getText(code));
        new Thread(() -> {
            long activationAvailableTime = 5 * 60 * 1000;
            try {
                sleep(activationAvailableTime);
                companyActivation.setActivationPossible(false);
                companyActivationRepository.save(companyActivation);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    //saljemo novu sifru ka User-servicu
    public Boolean setCompanyPassword(SetUserPasswordCodeDto setUserPasswordCodeDto) {
        CompanyActivation companyActivation = companyActivationRepository.findCompanyActivationByCodeAndActivationPossibleIsTrue(setUserPasswordCodeDto.getCode()).orElseThrow(
                () -> new NotFoundException("Activation code not found."));
        if (companyActivation.isActivationPossible()) {
            SetPasswordDto setPasswordDto = new SetPasswordDto();
            setPasswordDto.setEmail(setUserPasswordCodeDto.getEmail());
            setPasswordDto.setPassword(setUserPasswordCodeDto.getPassword());

            userServiceClient.setCompanyPassword(setPasswordDto);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void cleanupInactiveEntities() {
        try {
            // Get all entities with setActive == false
            Optional<CompanyActivation> inactiveEntities = companyActivationRepository.findByActivationPossible(false);

            // Delete inactive entities
            companyActivationRepository.deleteAll(inactiveEntities.stream().toList());
        } catch (Exception e) {
            System.err.println("Error occurred during entity cleanup: " + e.getMessage());
        }
    }

    protected String getSubject() {
        return "Company account activation";
    }

    protected String getText(Integer code) {
        return "Your activation code is:" + code;
    }
}
