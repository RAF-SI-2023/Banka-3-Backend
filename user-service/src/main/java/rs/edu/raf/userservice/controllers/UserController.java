package rs.edu.raf.userservice.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UserDto;
import rs.edu.raf.userservice.domains.dto.login.LoginRequest;
import rs.edu.raf.userservice.domains.dto.login.LoginResponse;
import rs.edu.raf.userservice.services.UserService;
import rs.edu.raf.userservice.utils.JwtUtil;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userService.getUserByEmail(loginRequest.getEmail()))));
    }

    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        return userService.getUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserDto createUserDto) {
        userService.addUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@RequestBody CreateUserDto createUserDto) {
        return userService.addUser(createUserDto);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@RequestBody UpdateUserDto updatedUser, @PathVariable Long id) {
        return userService.updateUser(updatedUser, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id) != null) {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping(value = "/findByMobileNum/{mobileNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserByMobileNumber(@PathVariable String mobileNumber) {
        return userService.getUserByMobileNumber(mobileNumber);
    }

    @GetMapping(value = "/findByJMBG/{jmbg}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserByJMBG(@PathVariable String jmbg) {
        return userService.getUserByJmbg(jmbg);

    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchUsers(@RequestParam(value = "firstName", required = false) String firstName,
                                             @RequestParam(value = "lastName", required = false) String lastName,
                                             @RequestParam(value = "email", required = false) String email) {
        return ResponseEntity.ok(this.userService.search(firstName, lastName, email));
    }
}
