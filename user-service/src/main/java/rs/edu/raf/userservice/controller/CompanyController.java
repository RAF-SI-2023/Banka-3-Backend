package rs.edu.raf.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.company.CompanyCreateDto;
import rs.edu.raf.userservice.domain.dto.login.LoginRequest;
import rs.edu.raf.userservice.domain.dto.login.LoginResponse;
import rs.edu.raf.userservice.domain.dto.user.UserSetPasswordDto;
import rs.edu.raf.userservice.service.CompanyService;
import rs.edu.raf.userservice.util.jwt.JwtUtil;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/company")
public class CompanyController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CompanyService companyService;

    @PostMapping("/auth/login")
    @Operation(description = "za Login Kompanije")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(companyService.getCompanyByEmail(loginRequest.getEmail()))));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "dohvatanje svih kompanija")
    public ResponseEntity<?> getAllCompanies() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/getByCompany/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "dohvatanje kompanije po IDu")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.findById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't find company with provided ID");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "dodavanje nove kompanije")
    public ResponseEntity<?> createCompany(@RequestBody CompanyCreateDto companyCreateDto) {
        return ResponseEntity.ok(companyService.create(companyCreateDto));
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "deaktiviranje kompanije po IDu")
    public ResponseEntity<?> deactivateCompany(@PathVariable Long id){
        if (companyService.changeCompanyActive(id, false)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Couldn't deactivate Company");
    }

    @PutMapping("/activate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BANKING_OFFICER')")
    @Operation(description = "aktiviranje kompanije po IDu")
    public ResponseEntity<?> activateCompany(@PathVariable Long id){
        if (companyService.changeCompanyActive(id, true)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Couldn't activate Company");
    }

    @GetMapping(value = "/isCompanyActive/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "provera da li je Company Code active")
    public ResponseEntity<?> isCompanyActive(@PathVariable String email) {
        try {
            return ResponseEntity.ok(companyService.isCompanyActive(email));
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    @PostMapping(value = "/setPassword")
    @Operation(description = "kada kompanija hoce da postavi prvi put sifru, ili da promeni postojecu")
    public ResponseEntity<?> setPassword(@RequestBody UserSetPasswordDto userSetPasswordDto) {
        try {
            companyService.setPassword(userSetPasswordDto);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Couldn't set password for Company with email: " + userSetPasswordDto.getEmail());
        }
    }
}

