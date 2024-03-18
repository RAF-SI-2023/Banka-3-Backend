package com.example.emailservice.service.impl;

import com.example.emailservice.client.UserServiceClient;
import com.example.emailservice.dto.SetPasswordDTO;

import com.example.emailservice.model.EmployeeActivation;

import com.example.emailservice.repository.EmployeeActivationRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeActivationRepository employeeActivationRepository;
    private final UserServiceClient userServiceClient;
    private final EmailService emailService;
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
        new Thread(()->{
            long activationAvailableTime = 5*60*60;
            try {
                sleep(activationAvailableTime);
                employeeActivation.setActivationPossible(false);
                employeeActivationRepository.save(employeeActivation);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public String changePassword(String identifier, String password) {
        EmployeeActivation employeeActivation =
                employeeActivationRepository.findEmployeeActivationByIdentifierAndActivationPossibleIsTrue(identifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activation is not possible"));
        SetPasswordDTO passwordDTO = new SetPasswordDTO(password, employeeActivation.getEmail());
        ResponseEntity<String> response = userServiceClient.setPassword(passwordDTO);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ResponseStatusException(response.getStatusCode(), response.getBody());
        }
        return "Password successfully changed";
    }

    protected String getSubject(){
        return "Employee account activation";
    }
    protected String getText(String identifier){
        return "http://localhost:8081/employee/changePassword/" + identifier;
    }
}