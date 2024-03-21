package com.example.emailservice.controller;

import com.example.emailservice.dto.TryPasswordResetDTO;
import com.example.emailservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/employee")
@RequiredArgsConstructor
public class EmailController {
    private final EmployeeService employeeService;

    /***
     *Kada se kreira employee, user service salje request na employeeCreated da bi se napravio
     *identifier koji vazi 5 minuta i
     *
     */
    @GetMapping("/employeeCreated")
    public ResponseEntity<Void> employeeCreated(@RequestParam(name = "email") String email) {
        employeeService.employeeCreated(email);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/setPassword/{identifier}")
    public ResponseEntity<String> changePassword(@PathVariable(name = "identifier") String identifier,
                                                 @RequestBody String password){
        String response = employeeService.changePassword(identifier, password);
        return ResponseEntity.ok(response);
    }

    /**
     * Kada zaposleni zatrazi promenu sifre, svoj email salje na ovu rutu
     * Meotda salje mejl na prosledjeni email iz request parametra
     * */
    @GetMapping("/resetPassword")
    public ResponseEntity<Void> tryResetPassword(@RequestParam(name = "email") String email) {
        employeeService.tryResetPassword(email);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Ova metoda se poziva nakon sto je zaposleni prosledio novu sifru, proverava validnost
     * identifera i menja sifru
     * */
    @PostMapping("/tryPasswordReset")
    public ResponseEntity<String> resetPassword(@RequestBody TryPasswordResetDTO tryPasswordResetDTO){
        return ResponseEntity.ok(employeeService.resetPassword(tryPasswordResetDTO));
    }

}
