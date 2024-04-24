package rs.edu.raf.userservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomDetailsService implements UserDetailsService {
    private final UserService userService;
    private final EmployeeService employeeService;
    private final CompanyService companyService;

    public CustomDetailsService(UserService userService, EmployeeService employeeService, CompanyService companyService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.companyService = companyService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userService.loadUserByUsername(email);
        } catch (Exception ex) {
            try {
                return companyService.loadUserByUsername(email);
            } catch (Exception e) {
                return employeeService.loadUserByUsername(email);
            }
        }
    }
}
