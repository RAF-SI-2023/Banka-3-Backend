package rs.edu.raf.userservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.services.UserService;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.LoginRequest;
import rs.edu.raf.userservice.domains.dto.RegistrerUserDto;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

//    private final UserService userService;

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void getAllUsers() {

    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Long id) {

////        return userService.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
           produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user){

////        return userService.save(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
           produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User updatedUser){

////        return userService.save(updatedUser);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        if(userService.findById(id) != null) {
////            userService.deleteById(id);
           return ResponseEntity.ok().build();
       // }
       return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByEmail(@PathVariable String email) {

////        return userService.findByEmail(email).orElse(null);
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByUsername(@PathVariable String username) {

//////        return userService.findByUsername(email).orElse(null);
   }

    @GetMapping(value = "/{mobileNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByMobileNumber(@PathVariable String mobileNumber) {

////        return userService.findByMobileNumber(mobileNumber).orElse(null);
    }

    @GetMapping(value = "/{jmbg}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByJMBG(@PathVariable String jmbg) {
////        return userService.findByJmbg(jmbg).orElse(null);
//
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(LoginRequest loginRequest) {

        User user = userService.findByEmail(loginRequest.getEmail());

        if(user != null) {
            if(user.getPassword().equals(loginRequest.getPassword())) {
                // ovde sada dodje jwt filter i generise token koji se vraca na front
            }
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(RegistrerUserDto registrerUserDto) {

        User user = User.builder()
                .firstName(registrerUserDto.getFirstName())
                .lastName(registrerUserDto.getLastName())
                .jmbg(registrerUserDto.getJmbg())
                .dateOfBirth(registrerUserDto.getDateOfBirth())
                .build();

        userService.addUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
