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
            if (userService.loadUserByUsername(email) != null){
                System.out.println("user service");
                return userService.loadUserByUsername(email);
            }
            if (companyService.loadUserByUsername(email) != null){
                System.out.println("company service, " + email);
                return companyService.loadUserByUsername(email);
            }
        } catch (Exception ex) {
            // Ako UserService ne može pronaći korisnika, pokušaj sa EmployeeService
            return employeeService.loadUserByUsername(email);
        }
        return null;
    }
}
