package com.example.emailservice.controller;

import com.example.emailservice.dto.password.SetUserPasswordCodeDto;
import com.example.emailservice.dto.password.TryPasswordResetDto;
import com.example.emailservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping(value = "/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/userActivation")
    @Operation(description = "ovu rutu ganja User-Service kako bi poslao Code na email korisnika koji se prvi put loguje")
    public ResponseEntity<?> userActivation(@RequestParam(name = "email") String email) {
        userService.userActivation(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activateUser")
    @Operation(description = "ovde nam stizu Code, password i email za Usera kom treba podesiti sifru")
    public ResponseEntity<?> setUserPassword(@RequestBody SetUserPasswordCodeDto setUserPasswordCodeDTO) {
        Boolean userActivated = userService.setUserPassword(setUserPasswordCodeDTO);
        if (userActivated) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/resetPassword")
    @Operation(description = "ovde stize email na koji treba poslati link ka stranici za resetu lozinke")
    public ResponseEntity<?> resetPassword(@RequestParam(name = "email") String email) {
        userService.generateResetCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tryPasswordReset")
    @Operation(description = "ovde stizu parametri identifier i password koji treba podesiti")
    public String tryChangePassword(@RequestBody TryPasswordResetDto tryPasswordResetDTO) {
        return userService.tryChangePassword(tryPasswordResetDTO);
    }
}
