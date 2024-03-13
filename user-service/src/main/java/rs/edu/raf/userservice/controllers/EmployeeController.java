package rs.edu.raf.userservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.services.EmoloyeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    /*
    @Autowired
    private EmoloyeeService emoloyeeService;

    @Autowired
    public EmployeeController(EmoloyeeService emoloyeeService){this.emoloyeeService=emoloyeeService;}


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto createEmployee(@RequestBody CreateEmployeeDto createEmployeeDto) {
        return emoloyeeService.createEmployee(createEmployeeDto);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto updateEmployee(@RequestBody UpdateEmployeeDto updatedEmployee, @PathVariable Long id) {
        return emoloyeeService.updateEmployee(updatedEmployee, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        if(emoloyeeService.findEmployeeById(id) != null) {
            emoloyeeService.deactivateEmployee(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeById(@PathVariable Long id) {
        return emoloyeeService.findEmployeeById(id).orElse(null);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findAllEmployees() {
        return emoloyeeService.findAllEmployees();
    }

    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByEmail(@PathVariable String email) {
        return emoloyeeService.findEmployeeByEmail(email);
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByUsername(@PathVariable String username) {
        return emoloyeeService.findEmployeeByUsername(username);
    }

    @GetMapping(value = "/{mobileNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByMobileNumber(@PathVariable String mobileNumber) {
        return emoloyeeService.findEmployeeByMobileNumber(mobileNumber);
    }

    @GetMapping(value = "/{jmbg}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByJMBG(@PathVariable String jmbg) {
        return emoloyeeService.findEmployeeByJmbg(jmbg);
    }

    @GetMapping(value = "/{position}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeDto findEmployeeByPosition(@PathVariable String position) {
        return emoloyeeService.findEmployeeByPosition(position);
    }

     */
}
