package rs.edu.raf.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.employee.*;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.login.LoginResponse;
import rs.edu.raf.userservice.service.EmployeeService;
import rs.edu.raf.userservice.util.jwt.JwtUtil;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmployeeService employeeService;

    @PostMapping("/auth/login")
    @Operation(description = "login zaposlenog")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
//                    loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(employeeService.findByEmail(loginRequest.getEmail()))));
    }

    @Cacheable(value = "allEmployees")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "vracam listu svih zaposlenih")
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.findAll();
    }


    @Cacheable(value = "employeeById", key = "#id")
    @GetMapping(value = "/findById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "vracam zaposlenog po ID-u")
    public EmployeeDto findEmployeeById(@PathVariable Long id) {
        try {
            return employeeService.findById(id);
        }catch (Exception e){
            return null;
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "allEmployees", allEntries = true),
            @CacheEvict(value = "searchEmployees", allEntries = true),
            @CacheEvict(value = "exchangeEmployees", allEntries = true),
    }, put = {
            @CachePut(value = "employeeByEmail", key = "#createEmployeeDto.email"),
            @CachePut(value = "employeeByUsername", key = "#createEmployeeDto.username")
    })

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "pravimo novog zaposlenog")
    public EmployeeDto createEmployee(@RequestBody EmployeeCreateDto createEmployeeDto) {
        try {
            return employeeService.addEmployee(createEmployeeDto);
        }catch (Exception e){
            return null;
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "allEmployees", allEntries = true),
            @CacheEvict(value = "searchEmployees", allEntries = true),
            @CacheEvict(value = "exchangeEmployees", allEntries = true),
    }, put = {
            @CachePut(value = "employeeById", key = "#id"),
            @CachePut(value = "employeeByEmail", key = "#employeeService.findById(#id).email"),
            @CachePut(value = "employeeByUsername", key = "#employeeService.findById(#id).username")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "izmena postojeceg zapolsenog")
    public EmployeeDto updateEmployee(@RequestBody EmployeeUpdateDto updatedEmployee, @PathVariable Long id) {
        try {
            return employeeService.updateEmployee(updatedEmployee, id);
        }catch (Exception e){
            return null;
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "allEmployees", allEntries = true),
            @CacheEvict(value = "searchEmployees", allEntries = true),
            @CacheEvict(value = "exchangeEmployees", allEntries = true),
            @CacheEvict(value = "employeeById", key = "#id"),
            @CacheEvict(value = "employeeByEmail", key = "#employeeService.findById(#id).email"),
            @CacheEvict(value = "employeeByUsername", key = "#employeeService.findById(#id).username")
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "deaktivacija naloga zaposlenog")
    public EmployeeDto deleteEmployee(@PathVariable Long id) {
        try {
            return employeeService.deactivateEmployee(id);
        }catch (Exception e){
            return null;
        }
    }

    @Cacheable(value = "employeeByEmail", key = "#email")
    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmployeeDto findEmployeeByEmail(@PathVariable String email) {
        try {
            return employeeService.findByEmail(email);
        }catch (Exception e){
            return null;
        }
    }


    @Cacheable(value = "employeeByUsername", key = "#username")
    @GetMapping(value = "/findByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByUsername(@PathVariable String username) {
        try {
            return employeeService.findByUsername(username);
        }catch (Exception e){
            return null;
        }
    }

    @Cacheable(value = "searchEmployees",key = "#firstName + #lastName + #email + #role")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<EmployeeDto> searchEmployees(@RequestParam(value = "firstName", required = false) String firstName,
                                             @RequestParam(value = "lastName", required = false) String lastName,
                                             @RequestParam(value = "email", required = false) String email,
                                             @RequestParam(value = "role", required = false) String role) {
        try {
            return employeeService.search(firstName, lastName, email, role);
        }catch (Exception e){
            return null;
        }
    }


    @PostMapping(value = "/setPassword")
    @Operation(description = "kada zaposleni prvi put postavlja sifru, ili da promeni postojecu")
    public ResponseEntity<String> changePassword(@RequestBody EmployeeSetPasswordDto passwordDto) {
        try {
            employeeService.setPassword(passwordDto);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't set password for User with email: " + passwordDto.getEmail());
        }
    }

    @Cacheable(value = "exchangeEmployees")
    @GetMapping("/getExchangeEmployees")
    @Operation(description = "dohvatamo sve zaposlene potrebne za Exchange Service")
    public List<ExchangeEmployeeDto> getExchangeEmployees(){
        try {
            return employeeService.getExchangeEmployees();
        }catch (Exception e){
            return null;
        }
    }
}
