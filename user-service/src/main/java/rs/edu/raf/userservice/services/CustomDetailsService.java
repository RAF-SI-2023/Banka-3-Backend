package rs.edu.raf.userservice.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomDetailsService implements UserDetailsService {
    private final UserService userService;
    private final EmployeeService employeeService;

    public CustomDetailsService(UserService userService, EmployeeService employeeService) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userService.loadUserByUsername(email);
        } catch (Exception ex) {
            // Ako UserService ne može pronaći korisnika, pokušaj sa EmployeeService
            return employeeService.loadUserByUsername(email);
        }
    }
}
