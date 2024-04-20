package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.edu.raf.userservice.domain.dto.employee.*;
import rs.edu.raf.userservice.domain.exception.ForbiddenException;
import rs.edu.raf.userservice.domain.exception.NotFoundException;
import rs.edu.raf.userservice.domain.mapper.EmployeeMapper;
import rs.edu.raf.userservice.domain.model.Employee;
import rs.edu.raf.userservice.domain.model.Permission;
import rs.edu.raf.userservice.domain.model.enums.RoleName;
import rs.edu.raf.userservice.repository.EmployeeRepository;
import rs.edu.raf.userservice.util.client.EmailServiceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailServiceClient emailServiceClient;


    public EmployeeDto create(EmployeeCreateDto employeeCreateDto) {

        Employee employee = EmployeeMapper.INSTANCE.employeeCreateDtoToEmployee(employeeCreateDto);
        employee.setIsActive(true);
        emailServiceClient.sendEmailToEmailService(employee.getEmail());
        employeeRepository.save(employee);
        return EmployeeMapper.INSTANCE.employeeToEmployeeDto(employee);
    }

    public EmployeeDto delete(Long id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("user with" + id + " not " +
                        "found"));
        employee.setIsActive(false);
        return EmployeeMapper.INSTANCE.employeeToEmployeeDto(employeeRepository.save(employee));
    }

    public EmployeeDto update(EmployeeUpdateDto employeeUpdateDto, Long id) {
        Employee employee =
                employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("employee with" + id + " not " +
                        "found"));

        employee.setIsActive(employeeUpdateDto.getIsActive());
        if (!employee.getIsActive()) {
            return null;
        }

        EmployeeMapper.INSTANCE.updateEmployeeFromEmployeeUpdateDto(employee, employeeUpdateDto);
        employeeRepository.save(employee);
        return EmployeeMapper.INSTANCE.employeeToEmployeeDto(employee);
    }

    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).orElseThrow(() -> new NotFoundException(
                "user with" + id + " not found"));
    }

    public EmployeeDto findByEmail(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        return employee.map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).orElseThrow(() -> new NotFoundException(
                "user with" + email + " not found"));
    }

    public EmployeeDto findByUsername(String username) {
        Optional<Employee> employee = employeeRepository.findByUsername(username);
        return employee.map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).orElseThrow(() -> new NotFoundException(
                "user with" + username + " not found"));
    }

    public List<EmployeeDto> search(String firstName, String lastName, String email, String role) {
        RoleName roleNameEnum;
        try {
            if (role.isEmpty()) {
                roleNameEnum = null;
            } else {
                roleNameEnum = RoleName.valueOf(role.toUpperCase());
            }
        } catch (Exception e) {
            throw new NotFoundException("No employees found matching the criteria");
        }
        List<Employee> employees = employeeRepository.findEmployees(firstName, lastName, email, roleNameEnum)
                .orElseThrow(() -> new NotFoundException("No employees found matching the criteria"));
        return employees.stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = this.employeeRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(
                "employee not found"));

        if (employee == null) {
            throw new UsernameNotFoundException("employee with the email: " + email + " not found");
        }
        if (!employee.getIsActive()) {
            throw new ForbiddenException("user not active");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Permission> permissions = employee.getPermissions();

        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getPermissionName().name()));
        }
        authorities.add(new SimpleGrantedAuthority(employee.getRole().getRoleName().toString()));

        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(),
                authorities);
    }

    public String setPassword(SetPasswordDTO passwordDTO) {
        Employee employee = employeeRepository.findByEmail(passwordDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        employee.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        employeeRepository.save(employee);
        return "Successfully updated password for " + passwordDTO.getEmail();
    }

    public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
        Employee employee = employeeRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        employee.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        employeeRepository.save(employee);
        return "Successfully reseted password for " + resetPasswordDTO.getEmail();
    }

    public List<ExchangeEmployeeDTO> findSupervisorsAndAgents() {
        Optional<List<Employee>> employees = employeeRepository.findSupervisorsAndAgents();
        List<ExchangeEmployeeDTO> exchangeEmployeeDTOS = new ArrayList<>();

        for(Employee employee : employees.get()){
            ExchangeEmployeeDTO exchangeEmployeeDTO = new ExchangeEmployeeDTO();
            exchangeEmployeeDTO.setEmployeeId(employee.getEmployeeId());
            exchangeEmployeeDTO.setEmail(employee.getEmail());
            exchangeEmployeeDTO.setRole(String.valueOf(employee.getRole().getRoleName()));
            exchangeEmployeeDTOS.add(exchangeEmployeeDTO);
        }

        return exchangeEmployeeDTOS;
    }

}
