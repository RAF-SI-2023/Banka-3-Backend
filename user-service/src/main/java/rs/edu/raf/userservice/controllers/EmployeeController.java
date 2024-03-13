package rs.edu.raf.userservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.EmployeeUpdateDto;
import rs.edu.raf.userservice.services.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService emoloyeeService;

    @Autowired
    public EmployeeController(EmployeeService emoloyeeService) {
        this.emoloyeeService = emoloyeeService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto createEmployee(@RequestBody EmployeeCreateDto createEmployeeDto) {
        return emoloyeeService.create(createEmployeeDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> findAllEmployees() {
        return emoloyeeService.findAll();
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto updateEmployee(@RequestBody EmployeeUpdateDto updatedEmployee, @PathVariable Long id) {
        return emoloyeeService.update(updatedEmployee, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        if (emoloyeeService.findById(id) != null) {
            emoloyeeService.delete(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeById(@PathVariable Long id) {
        return emoloyeeService.findById(id);
    }

    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByEmail(@PathVariable String email) {
        return emoloyeeService.findByEmail(email);
    }

    @GetMapping(value = "/findByUsername/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByUsername(@PathVariable String username) {
        return emoloyeeService.findByUsername(username);
    }

    @GetMapping(value = "/findByMobileNumber/{mobileNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByMobileNumber(@PathVariable String mobileNumber) {
        return emoloyeeService.findByMobileNumber(mobileNumber);
    }

    @GetMapping(value = "/findByJmbg/{jmbg}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByJMBG(@PathVariable String jmbg) {
        return emoloyeeService.findByJmbg(jmbg);
    }

    @GetMapping(value = "/findByPosition/{position}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> findEmployeeByPosition(@PathVariable String position) {
        return emoloyeeService.findByPosition(position);
    }

}
