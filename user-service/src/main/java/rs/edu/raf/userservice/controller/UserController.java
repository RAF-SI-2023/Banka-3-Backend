package rs.edu.raf.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.login.LoginResponse;
import rs.edu.raf.userservice.domain.dto.user.SetPasswordDto;
import rs.edu.raf.userservice.domain.dto.user.UserPostPutDto;
import rs.edu.raf.userservice.service.UserService;
import rs.edu.raf.userservice.util.jwt.JwtUtil;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    @Operation(description = "za Login korisnika")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userService.getUserByEmail(loginRequest.getEmail()))));
    }

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "vracam listu svih korsinika")
    public ResponseEntity<?> getAllUsers() {
        try{
            return ResponseEntity.ok(userService.getUsers());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't get all users");
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "vraca korisnika sa odgovarajucim id-om")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Coundn't find User with id: " + id);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "pravimo novog korisnika")
    public ResponseEntity<?> createUser(@RequestBody UserPostPutDto userPostPutDto) {
        return ResponseEntity.ok(userService.addUser(userPostPutDto));
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    public ResponseEntity<?> updateUser(@RequestBody UserPostPutDto updatedUser, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.updateUser(updatedUser, id));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Couldn't update user");
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id) != null) {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "na osnovu Email-a vraca User-a")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find user with email: " + email);
        }
    }

    @GetMapping(value = "/findEmailById/{userId}")
    @Operation(description = "na osnovu ID-a vraca korisnikov email")
    public ResponseEntity<?> getUSerEmailById(@PathVariable Long userId){
        try {
            return ResponseEntity.ok(userService.getEmailByUser(userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Coulnd't find user with id: " + userId);
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchUsers(@RequestParam(value = "firstName", required = false) String firstName,
                                         @RequestParam(value = "lastName", required = false) String lastName,
                                         @RequestParam(value = "email", required = false) String email) {
        return ResponseEntity.ok(this.userService.search(firstName, lastName, email));
    }

    @GetMapping(value = "/isUserActive/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> isUserActive(@PathVariable String email) {
        return ResponseEntity.ok(userService.isUserActive(email));
    }

    @PostMapping(value = "/setPassword")
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordDto setPasswordDTO) {
        return ResponseEntity.ok(userService.setPassword(setPasswordDTO));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody SetPasswordDto setPasswordDto) {
        userService.resetPassword(setPasswordDto);
        return ResponseEntity.ok(userService.resetPassword(setPasswordDto));
    }
}
