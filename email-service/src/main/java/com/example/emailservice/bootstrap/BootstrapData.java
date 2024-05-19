package com.example.emailservice.bootstrap;

import com.example.emailservice.domain.model.*;
import com.example.emailservice.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final CompanyActivationRepository companyActivationRepository;
    private final EmployeeActivationRepository employeeActivationRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final TransactionActivationRepository transactionActivationRepository;
    private final UserActivationRepository userActivationRepository;


    @Override
    public void run(String... args)  {

        CompanyActivation companyActivation1 = new CompanyActivation();
        companyActivation1.setEmail("firma1@gmail.com");
        companyActivation1.setCode(11);
        companyActivation1.setTimeCreated(LocalDateTime.of(2024,1,15,5,25));
        companyActivation1.setActivationPossible(true);

        CompanyActivation companyActivation2 = new CompanyActivation();
        companyActivation2.setEmail("firma2@gmail.com");
        companyActivation2.setCode(12);
        companyActivation2.setTimeCreated(LocalDateTime.of(2024,3,1,12,45));
        companyActivation2.setActivationPossible(false);

        if (companyActivationRepository.count() == 0) {
            loadCompanyActivationData(List.of(companyActivation1,companyActivation2));
        }
        EmployeeActivation employeeA = new EmployeeActivation();
        employeeA.setEmail("zaposleni123@gmail.com");
        employeeA.setIdentifier("zaposleni123");
        employeeA.setActivationTimestamp(LocalDateTime.of(2023,12,1,14,30));
        employeeA.setActivationPossible(true);

        EmployeeActivation employeeB = new EmployeeActivation();
        employeeB.setEmail("zaposleni222@gmail.com");
        employeeB.setIdentifier("zaposleni222");
        employeeB.setActivationTimestamp(LocalDateTime.of(2024,1,17,1,50));
        employeeB.setActivationPossible(true);

        if (employeeActivationRepository.count() == 0) {
            loadEmployeeActivationData(List.of(employeeA,employeeB));
        }

        PasswordReset sifra1 = new PasswordReset();
        sifra1.setEmail("zaposleni222@gmail.com");
        sifra1.setIdentifier("1");
        sifra1.setDate(LocalDateTime.now());
        sifra1.setActive(true);

        PasswordReset sifra2 = new PasswordReset();
        sifra2.setEmail("firma2@gmail.com");
        sifra2.setIdentifier("2");
        sifra2.setDate(LocalDateTime.now());
        sifra2.setActive(true);

        if (passwordResetRepository.count() == 0) {
            loadPasswordResetData(List.of(sifra1,sifra2));
        }

        TransactionActivation transaction1 = new TransactionActivation();
        transaction1.setEmail("zaposleni222@gmail.com");
        transaction1.setCode(123);
        transaction1.setTimeCreated(LocalDateTime.now());
        transaction1.setActive(true);

        TransactionActivation transaction2 = new TransactionActivation();
        transaction2.setEmail("user@gmail.com");
        transaction2.setCode(44);
        transaction2.setTimeCreated(LocalDateTime.now());
        transaction2.setActive(true);

        if (transactionActivationRepository.count() == 0) {
            loadTransactionActivationData(List.of(transaction1,transaction2));
        }

        UserActivation userJanko = new UserActivation();
        userJanko.setEmail("janko@gmail.com");
        userJanko.setCode(55);
        userJanko.setTimeCreated(LocalDateTime.now());
        userJanko.setActivationPossible(true);

        UserActivation userMarko = new UserActivation();
        userMarko.setEmail("marko@gmail.com");
        userMarko.setCode(23);
        userMarko.setTimeCreated(LocalDateTime.now());
        userMarko.setActivationPossible(false);

        if (userActivationRepository.count() == 0) {
            loadUserActivationData(List.of(userJanko,userMarko));
        }
    }
    private void loadCompanyActivationData(List<CompanyActivation> companyActivations) {
        companyActivationRepository.saveAll(companyActivations);
    }
    private void loadEmployeeActivationData(List<EmployeeActivation> employeeActivations) {
        employeeActivationRepository.saveAll(employeeActivations);
    }
    private void loadPasswordResetData(List<PasswordReset> passwordResets) {
        passwordResetRepository.saveAll(passwordResets);
    }
    private void loadTransactionActivationData(List<TransactionActivation> transactionActivations) {
        transactionActivationRepository.saveAll(transactionActivations);
    }
    private void loadUserActivationData(List<UserActivation> userActivations) {
        userActivationRepository.saveAll(userActivations);
    }

}
