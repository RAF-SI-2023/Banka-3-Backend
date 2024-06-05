package com.example.emailservice.bootstrap;

import com.example.emailservice.repository.CompanyActivationRepository;
import com.example.emailservice.repository.EmployeeActivationRepository;
import com.example.emailservice.repository.PasswordResetRepository;
import com.example.emailservice.repository.TransactionActivationRepository;
import com.example.emailservice.repository.UserActivationRepository;
import com.example.emailservice.service.mongo.MongoService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {
    
    private final CompanyActivationRepository companyActivationRepository;
    private final EmployeeActivationRepository employeeActivationRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final TransactionActivationRepository transactionActivationRepository;
    private final UserActivationRepository userActivationRepository;
    private final MongoService mongoService;
    
    @Override
    public void run(String... args) {
        mongoService.clearDatabase();
    }
}
