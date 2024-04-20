package rs.edu.raf.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.company.CompanyDto;
import rs.edu.raf.userservice.domain.dto.company.CreateCompanyDto;
import rs.edu.raf.userservice.service.CompanyService;
import rs.edu.raf.userservice.util.jwt.JwtUtil;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/company")
public class CompanyController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public CompanyDto createCompany(@RequestBody CreateCompanyDto companyCreateDto) {
        return companyService.create(companyCreateDto);
    }

    @GetMapping("/getAll")
    public List<CompanyDto> getAllCompanies() {
        return companyService.findAll();
    }

    @GetMapping("/getByCompany/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id) {
        return companyService.findById(id);
    }
}

