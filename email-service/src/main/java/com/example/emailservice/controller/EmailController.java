package com.example.emailservice.controller;

import com.example.emailservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/employee")
@RequiredArgsConstructor
public class EmailController {
    private final EmployeeService employeeService;
    @GetMapping("employeeCreated")
    public ResponseEntity<Void> employeeCreated(@RequestParam(name = "email") String email) {
        employeeService.employeeCreated(email);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @PostMapping("changePassword/{identifier}")
    public ResponseEntity<String> changePassword(@PathVariable(name = "identifier") String identifier,
                                                 @RequestBody String password){
        return ResponseEntity.ok(employeeService.changePassword(identifier, password));
    }
}
