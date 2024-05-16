package rs.edu.raf.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.employee.*;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.login.LoginResponse;
import rs.edu.raf.userservice.service.EmployeeService;
import rs.edu.raf.userservice.util.jwt.JwtUtil;

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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(employeeService.findByEmail(loginRequest.getEmail()))));
    }

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "vracam listu svih zaposlenih")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping(value = "/findById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "vracam zaposlenog po ID-u")
    public ResponseEntity<?> findEmployeeById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(employeeService.findById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find Employee withd id: " + id);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "pravimo novog zaposlenog")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeCreateDto createEmployeeDto) {
        try {
            return ResponseEntity.ok(employeeService.addEmployee(createEmployeeDto));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't add Employee");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "izmena postojeceg zapolsenog")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeUpdateDto updatedEmployee, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(employeeService.updateEmployee(updatedEmployee, id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't update Employee with id: " + id);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "deaktivacija naloga zaposlenog")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(employeeService.deactivateEmployee(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't deactivate Employee id:" + id);
        }
    }

    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findEmployeeByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(employeeService.findByEmail(email));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find employee with email: " + email);
        }
    }


    @GetMapping(value = "/findByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findEmployeeByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(employeeService.findByUsername(username));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find Employe with username: " + username);
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> searchEmployees(@RequestParam(value = "firstName", required = false) String firstName,
                                             @RequestParam(value = "lastName", required = false) String lastName,
                                             @RequestParam(value = "email", required = false) String email,
                                             @RequestParam(value = "role", required = false) String role) {
        try {
            return ResponseEntity.ok(this.employeeService.search(firstName, lastName, email, role));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Something went wrong");
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

    @GetMapping("/getExchangeEmployees")
    @Operation(description = "dohvatamo sve zaposlene potrebne za Exchange Service")
    public ResponseEntity<?> getExchangeEmployees(){
        try {
            return ResponseEntity.ok(employeeService.getExchangeEmployees());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
