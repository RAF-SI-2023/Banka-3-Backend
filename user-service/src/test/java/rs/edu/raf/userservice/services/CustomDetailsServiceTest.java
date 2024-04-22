package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomDetailsServiceTest {
//    @Mock
//    private UserService userService;
//    @Mock
//    private EmployeeService employeeService;
//
//    @Test
//    public void testLoadUserByUsername_UserFoundInUserService() {
//        // Priprema testnih podataka
//        String email = "test@example.com";
//        UserDetails userDetailsFromUserService = new User("username", "password", Collections.emptyList());
//
//        // Podešavanje ponašanja mock-ova
//        when(userService.loadUserByUsername(email)).thenReturn(userDetailsFromUserService);
//
//        // Izvršavanje metode koju testiramo
//        CustomDetailsService userDetailsService = new CustomDetailsService(userService, employeeService);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//        // Provera rezultata
//        assertNotNull(userDetails);
//        assertEquals(userDetailsFromUserService, userDetails);
//        verify(userService, times(1)).loadUserByUsername(email); // Provera da li je metoda pozvana tačno jednom
//        verify(employeeService, never()).loadUserByUsername(anyString()); // Provera da li je metoda na employeeService nikada nije pozvana
//    }
//
//    @Test
//    public void testLoadUserByUsername_UserFoundInEmployeeService() {
//        // Priprema testnih podataka
//        String email = "test@example.com";
//        UserDetails userDetailsFromEmployeeService = new User("test", "password", Collections.emptyList());
//
//        // Podešavanje ponašanja mock-ova
//        when(userService.loadUserByUsername(email)).thenThrow(new UsernameNotFoundException("User not found")); // Simulacija da korisnik nije pronađen u userService
//        when(employeeService.loadUserByUsername(email)).thenReturn(userDetailsFromEmployeeService);
//
//        // Izvršavanje metode koju testiramo
//        CustomDetailsService userDetailsService = new CustomDetailsService(userService, employeeService);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//        // Provera rezultata
//        assertNotNull(userDetails);
//        assertEquals(userDetailsFromEmployeeService, userDetails);
//        verify(userService, times(1)).loadUserByUsername(email); // Provera da li je metoda pozvana tačno jednom na userService
//        verify(employeeService, times(1)).loadUserByUsername(email); // Provera da li je metoda pozvana tačno jednom na employeeService
//    }
}