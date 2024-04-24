package com.example.emailservice.service;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.domain.dto.password.SetPasswordDto;
import com.example.emailservice.domain.dto.password.TryPasswordResetDto;
import com.example.emailservice.domain.model.EmployeeActivation;
import com.example.emailservice.repository.EmployeeActivationRepository;
import com.example.emailservice.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmailService emailService;
    private final UserServiceClient userServiceClient;
    private final EmployeeActivationRepository employeeActivationRepository;

    /***
     *Pravi se random identifier, objekat employeeActivation i cuva se u bazu
     *Posalje se mejl i onda se startuje timer da se za 5 minuta prebaci u false
     *
     */
    public void employeeCreated(String email) {
        String identifier = UUID.randomUUID().toString();
        EmployeeActivation employeeActivation = new EmployeeActivation(
                null,
                email,
                identifier,
                LocalDateTime.now(),
                true);
        employeeActivationRepository.save(employeeActivation);
        emailService.sendSimpleMessage(email, getSubject(), getText(identifier));
        new Thread(() -> {
            long activationAvailableTime = 5 * 60 * 1000;
            try {
                sleep(activationAvailableTime);
                employeeActivation.setActivationPossible(false);
                employeeActivationRepository.save(employeeActivation);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /***
     *Prvo se trazi u bazi da li moze nalog da se aktivira(ako ne moze baca bad request exception)
     *Ako postoji u bazi email i sifra se salju na userService.
     *Proverava se da li je response 200 i ako jeste stize poruka da je sifra uspesno promenjena
     */
    public String changePassword(String identifier, String password) {
        Optional<EmployeeActivation> employeeActivationOptional =
                employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier);
        EmployeeActivation employeeActivation = employeeActivationOptional.get();

        SetPasswordDto setPasswordDto = new SetPasswordDto();
        setPasswordDto.setEmail(employeeActivation.getEmail());
        setPasswordDto.setPassword(password);

        ResponseEntity<String> response = userServiceClient.setEmployeePassword(setPasswordDto);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }
        return "Password successfully changed";
    }

    /**
     * Metoda pravi identifer i proverava da li zaposleni postoji u bazi da bi poslala email
     * i postavlja tajmer na pet minuta, ako prodje pet minuta, link nije vlaidan
     */
    public void tryResetPassword(String email) {
        String identifier = UUID.randomUUID().toString();
        EmployeeActivation employeeActivation = new EmployeeActivation(
                null,
                email,
                identifier,
                LocalDateTime.now(),
                true);
        employeeActivationRepository.save(employeeActivation);
        emailService.sendSimpleMessage(email, getReturnValue(), getLocation(identifier));
        new Thread(() -> {
            long activationAvailableTime = 300000;
            try {
                sleep(activationAvailableTime);
                employeeActivation.setActivationPossible(false);
                employeeActivationRepository.save(employeeActivation);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Salje se nova sifra user servisu ukoliko je identifer dobar
     */
    public void resetPassword(TryPasswordResetDto tryPasswordResetDto) {
        EmployeeActivation employeeActivation =
                employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(tryPasswordResetDto.getIdentifier())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activation is not possible"));

        SetPasswordDto resetPasswordDto = new SetPasswordDto();
        resetPasswordDto.setEmail(employeeActivation.getEmail());
        resetPasswordDto.setPassword(tryPasswordResetDto.getPassword());

        ResponseEntity<String> response = userServiceClient.setEmployeePassword(resetPasswordDto);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }
    }

    protected String getSubject() {
        return "Employee account activation";
    }

    protected String getText(String identifier) {
        return "http://localhost:80/change-password/" + identifier;
    }

    protected String getReturnValue() {
        return "Employee asked for password change";
    }

    protected String getLocation(String identifier) {
        return "http://localhost:80/employee/password-confirm/" + identifier;
    }
}
