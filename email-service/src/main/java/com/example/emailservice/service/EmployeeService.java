package com.example.emailservice.service;

public interface EmployeeService {
    void employeeCreated(String email);
    String changePassword(String identifier, String password);
    void tryResetPassword(String email);
    String resetPassword(String identifier, String newPassword);
}
