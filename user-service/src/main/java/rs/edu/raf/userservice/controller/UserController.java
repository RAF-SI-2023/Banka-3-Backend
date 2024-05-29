package rs.edu.raf.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.login.LoginResponse;
import rs.edu.raf.userservice.domain.dto.user.*;
import rs.edu.raf.userservice.service.UserService;
import rs.edu.raf.userservice.util.jwt.JwtUtil;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

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

    @Cacheable(value = "allUsers")
    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "vracam listu svih korsinika")
    public List<UserDto> getAllUsers() {
        try{
            return userService.getUsers();
        }catch (Exception e){
            return null;
        }
    }

    @Cacheable(value = "userById", key = "#id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "vraca korisnika sa odgovarajucim id-om")
    public UserDto getUserById(@PathVariable Long id) {
        try {
            return userService.getUserById(id);
        }catch (Exception e){
            return null;
        }
    }


    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "searchUsers", allEntries = true)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "pravimo novog korisnika")
    public UserDto createUser(@RequestBody UserPostPutDto userPostPutDto) {
        try {
            return userService.addUser(userPostPutDto);
        }catch (Exception e){
            return null;
        }
    }

    @Caching(put = {
            @CachePut(value = "userById", key = "#id"),
            @CachePut(value = "userEmailById", key = "#id"),
            @CachePut(value = "userByEmail", key = "#result.body.email")
    }, evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "searchUsers", allEntries = true)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "izmena postojeceg korisnika")
    public UserDto updateUser(@RequestBody UserPostPutDto userPostPutDto, @PathVariable Long id) {
        try {
            return userService.updateUser(userPostPutDto, id);
        }catch (Exception e) {
            return null;
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "userById", key = "#id"),
            @CacheEvict(value = "userByEmail", key = "#result.body.email"),
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "userActive", key = "#result.body.email"),
            @CacheEvict(value = "searchUsers", allEntries = true),
            @CacheEvict(value = "userEmailById", key = "#id")
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "deaktivacija naloga korisnika")
    public UserDto deleteUser(@PathVariable Long id) {
        try {
            return userService.deactivateUser(id);
        }catch (Exception e){
            return null;
        }
    }

    @Cacheable(value = "userByEmail", key = "#email")
    @GetMapping(value = "/findByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "na osnovu Email-a vraca User-a")
    public UserDto getUserByEmail(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email);
        }catch (Exception e){
            return null;
        }
    }

    @Cacheable(value = "userEmailById", key = "#userId")
    @GetMapping(value = "/findEmailById/{userId}")
    @Operation(description = "na osnovu ID-a korisnika vraca korisnikov email")
    public UserEmailDto getUSerEmailById(@PathVariable Long userId){
        try {
            return userService.getEmailByUser(userId);
        }catch (Exception e){
            return null;
        }
    }


    @Cacheable(value = "searchUsers", key = "#firstName + '_' + #lastName + '_' + #email")
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "pretraga korisnika po parametrima")
    public List<UserDto> searchUsers(@RequestParam(value = "firstName", required = false) String firstName,
                                     @RequestParam(value = "lastName", required = false) String lastName,
                                     @RequestParam(value = "email", required = false) String email) {
        try {
            return userService.search(firstName, lastName, email);
        }catch (Exception e){
            return null;
        }
    }

    @Cacheable(value = "userActive", key = "#email")
    @GetMapping(value = "/isUserActive/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "provera da li je korisnik Code active")
    public IsUserActiveDto isUserActive(@PathVariable String email) {
        try {
            return userService.isUserActive(email);
        } catch (Exception e){
            return null;
        }
    }


    //Ne treba da se kesira,jer se nikad nece vracati password na front
    @PostMapping(value = "/setPassword")
    @Operation(description = "kada korisnik hoce da postavi prvi put sifru, ili da promeni postojecu")
    public ResponseEntity<?> setPassword(@RequestBody UserSetPasswordDto userSetPasswordDto) {
        try {
            userService.setPassword(userSetPasswordDto);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't set password for User with email: " + userSetPasswordDto.getEmail());
        }
    }
}
