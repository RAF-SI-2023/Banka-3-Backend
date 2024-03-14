package rs.edu.raf.userservice.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.EmployeeUpdateDto;
import rs.edu.raf.userservice.domains.dto.login.LoginRequest;
import rs.edu.raf.userservice.domains.dto.login.LoginResponse;
import rs.edu.raf.userservice.services.EmployeeService;
import rs.edu.raf.userservice.utils.JwtUtil;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(employeeService.findByEmail(loginRequest.getEmail()))));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody EmployeeCreateDto employeeCreateDto) {
        employeeService.create(employeeCreateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto createEmployee(@RequestBody EmployeeCreateDto createEmployeeDto) {
        return employeeService.create(createEmployeeDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> findAllEmployees() {
        return employeeService.findAll();
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto updateEmployee(@RequestBody EmployeeUpdateDto updatedEmployee, @PathVariable Long id) {
        return employeeService.update(updatedEmployee, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        if (employeeService.findById(id) != null) {
            employeeService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('READ_USERS')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByEmail(@PathVariable String email) {
        return employeeService.findByEmail(email);
    }

    @GetMapping(value = "/findByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByUsername(@PathVariable String username) {
        return employeeService.findByUsername(username);
    }

    @GetMapping(value = "/findByMobileNumber/{mobileNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByMobileNumber(@PathVariable String mobileNumber) {
        return employeeService.findByMobileNumber(mobileNumber);
    }

    @GetMapping(value = "/findByJmbg/{jmbg}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByJMBG(@PathVariable String jmbg) {
        return employeeService.findByJmbg(jmbg);
    }

    @GetMapping(value = "/findByPosition/{position}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> findEmployeeByPosition(@PathVariable String position) {
        return employeeService.findByPosition(position);
    }

}