package com.example.emailservice.service.impl;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.dto.ResetPasswordDTO;
import com.example.emailservice.dto.SetPasswordDTO;
import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.model.EmployeeActivation;
import com.example.emailservice.repository.EmployeeActivationRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeActivationRepository employeeActivationRepository;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;

    /***
     *Pravi se random identifier, objekat employeeActivation i cuva se u bazu
     *Posalje se mejl i onda se startuje timer da se za 5 minuta prebaci u false
     *
     */
    @Override
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
    @Override
    public String changePassword(String identifier, String password) {
        Optional<EmployeeActivation> employeeActivationOptional =
                employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier);
        EmployeeActivation employeeActivation = employeeActivationOptional.get();
        SetPasswordDTO passwordDTO = new SetPasswordDTO(password, employeeActivation.getEmail());
        ResponseEntity<String> response = userServiceClient.setPassword(passwordDTO);
        System.out.println(response.toString());
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }
        return "Password successfully changed";
    }

    /**
     * Metoda pravi identifer i proverava da li zaposleni postoji u bazi da bi poslala email
     * i postavlja tajmer na pet minuta, ako prodje pet minuta, link nije vlaidan
     */
    @Override
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
    @Override
    public String resetPassword(TryPasswordResetDTO tryPasswordResetDTO) {
        EmployeeActivation employeeActivation =
                employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(tryPasswordResetDTO.getIdentifier())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activation is not " +
                                "possible"));
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(tryPasswordResetDTO.getPassword(),
                employeeActivation.getEmail());
        ResponseEntity<String> response = userServiceClient.resetPassword(resetPasswordDTO);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }
        return "Password successfully changed";
    }

    protected String getSubject() {
        return "Employee account activation";
    }

    protected String getText(String identifier) {
        return "http://localhost:4200/change-password/" + identifier;
    }

    protected String getReturnValue() {
        return "Employee asked for password change";
    }

    protected String getLocation(String identifier) {
        return "http://localhost:4200/employee/password-confirm/" + identifier;
    }
}
