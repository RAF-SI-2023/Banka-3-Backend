
package rs.edu.raf.userservice.controllers;


import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.RegisterUserDto;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.services.UserService;

import rs.edu.raf.userservice.domains.dto.LoginRequest;
import rs.edu.raf.userservice.utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    public UserController() {
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void getAllUsers() {

    }

    // ovo je kompletno
    @PostMapping("/login")
    public ResponseEntity<?> login(LoginRequest loginRequest) {

        User user = userService.findByEmail(loginRequest.getEmail());

        if (user != null) {
            if (user.getPassword().equals(loginRequest.getPassword())) {
                /// izmeniti u JWT utls da se u claim stavi email od korisnika, a ne username
                return new ResponseEntity<>(jwtUtil.generateToken(user.getEmail()),HttpStatus.OK);
            }
            return new ResponseEntity<>("Pogresan password !",HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>("Pogresan email !",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(RegisterUserDto registerUserDto) {

        User user = User.builder()
                .firstName(registerUserDto.getFirstName())
                .lastName(registerUserDto.getLastName())
                .jmbg(registerUserDto.getJmbg())
                .dateOfBirth(registerUserDto.getDateOfBirth())
                .gender(registerUserDto.getGender())
                .phoneNumber(registerUserDto.getPhoneNumber())
                .address(registerUserDto.getAddress())
                .email(registerUserDto.getEmail())
                .build();

        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Long id) {

////        return userService.findById(id).orElse(null);
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) {

////        return userService.save(user);
        return null;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User updatedUser) {

////        return userService.save(updatedUser);
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        if(userService.findById(id) != null) {
////            userService.deleteById(id);
//        return ResponseEntity.ok().build();
        // }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByEmail(@PathVariable String email) {

////        return userService.findByEmail(email).orElse(null);
        return null;
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByUsername(@PathVariable String username) {

//////        return userService.findByUsername(email).orElse(null);
        return null;
    }

    @GetMapping(value = "/{mobileNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByMobileNumber(@PathVariable String mobileNumber) {

////        return userService.findByMobileNumber(mobileNumber).orElse(null);
        return null;
    }

    @GetMapping(value = "/{jmbg}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByJMBG(@PathVariable String jmbg) {
////        return userService.findByJmbg(jmbg).orElse(null);
//
        return null;
    }
}
