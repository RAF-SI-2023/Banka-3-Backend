package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rs.edu.raf.userservice.service.CompanyService;
import rs.edu.raf.userservice.service.CustomDetailsService;
import rs.edu.raf.userservice.service.EmployeeService;
import rs.edu.raf.userservice.service.UserService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomDetailsServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private EmployeeService employeeService;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CustomDetailsService customDetailsService;

    @Test
    public void testLoadUserByUsername_UserService() {
        // Priprema
        String email = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(email)).thenReturn(userDetails);

        // Izvršavanje
        UserDetails result = customDetailsService.loadUserByUsername(email);

        // Provjera
        verify(userService, times(1)).loadUserByUsername(email);
        verify(companyService, never()).loadUserByUsername(email);
        verify(employeeService, never()).loadUserByUsername(email);
    }

    @Test
    public void testLoadUserByUsername_CompanyService() {
        String email = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(email)).thenThrow(new UsernameNotFoundException("User not found"));
        when(companyService.loadUserByUsername(email)).thenReturn(userDetails);

        UserDetails result = customDetailsService.loadUserByUsername(email);

        verify(userService, times(1)).loadUserByUsername(email);
        verify(companyService, times(1)).loadUserByUsername(email);
        verify(employeeService, never()).loadUserByUsername(email);
    }

    @Test
    public void testLoadUserByUsername_EmployeeService() {
        String email = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(email)).thenThrow(new UsernameNotFoundException("User not found"));
        when(companyService.loadUserByUsername(email)).thenThrow(new UsernameNotFoundException("Company not found"));
        when(employeeService.loadUserByUsername(email)).thenReturn(userDetails);

        UserDetails result = customDetailsService.loadUserByUsername(email);

        verify(userService, times(1)).loadUserByUsername(email);
        verify(companyService, times(1)).loadUserByUsername(email);
        verify(employeeService, times(1)).loadUserByUsername(email);
    }
}