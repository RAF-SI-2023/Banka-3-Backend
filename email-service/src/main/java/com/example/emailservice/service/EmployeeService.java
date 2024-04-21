package com.example.emailservice.service;

import com.example.emailservice.dto.TryPasswordResetDto;

public interface EmployeeService {
    void employeeCreated(String email);

    String changePassword(String identifier, String password);

    void tryResetPassword(String email);

    String resetPassword(TryPasswordResetDto tryPasswordResetDTO);
}
