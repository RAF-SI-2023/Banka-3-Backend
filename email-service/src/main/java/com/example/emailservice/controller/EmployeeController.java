package com.example.emailservice.controller;

import com.example.emailservice.dto.password.TryPasswordResetDto;
import com.example.emailservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(value = "/api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    /***
     *Kada se kreira employee, user service salje request na employeeCreated da bi se napravio
     *identifier koji vazi 5 minuta i
     *
     */
    @GetMapping("/employeeCreated")
    @Operation(description = "ovu rutu ganja User-Service kada se doda zaposleni")
    public ResponseEntity<?> employeeCreated(@RequestParam(name = "email") String email) {
        employeeService.employeeCreated(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setPassword/{identifier}")
    @Operation(description = "employee postavlja sifru prvi put")
    public ResponseEntity<String> changePassword(@PathVariable(name = "identifier") String identifier,
                                                 @RequestBody String password) {
        String response = employeeService.changePassword(identifier, password);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resetPassword")
    @Operation(description = "Kada zaposleni zatrazi promenu sifre, svoj email salje na ovu rutu. Meotda salje mejl na prosledjeni email iz request parametra")
    public ResponseEntity<?> tryResetPassword(@RequestParam(name = "email") String email) {
        employeeService.tryResetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tryPasswordReset")
    @Operation(description = "Ova metoda se poziva nakon sto je zaposleni prosledio novu sifru, proverava validnost identifera i menja sifru")
    public ResponseEntity<?> resetPassword(@RequestBody TryPasswordResetDto tryPasswordResetDTO) {
        try {
            employeeService.resetPassword(tryPasswordResetDTO);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

}
