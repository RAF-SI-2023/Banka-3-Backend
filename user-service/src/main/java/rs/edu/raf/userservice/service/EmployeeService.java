package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
import rs.edu.raf.userservice.util.client.ExchangeServiceClient;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {
    private final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final EmailServiceClient emailServiceClient;
    private final ExchangeServiceClient exchangeServiceClient;

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

        return new User(employee.getEmail(), employee.getPassword(), authorities);
    }


    public EmployeeDto findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).orElseThrow(() -> new NotFoundException(
                "user with" + id + " not found"));
    }

    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto addEmployee(EmployeeCreateDto employeeCreateDto) {
        if (!emailPattern.matcher(employeeCreateDto.getEmail()).matches()) {
            throw new ValidationException("invalid email");
        }
        Employee employee = EmployeeMapper.INSTANCE.employeeCreateDtoToEmployee(employeeCreateDto);
        employee.setIsActive(true);
        Employee addedEmployee = employeeRepository.save(employee);

        emailServiceClient.sendEmailToEmailService(addedEmployee.getEmail());    //slanje mail-a za aktivaciju

        //kad se doda zapolsnei, da se posalje Exchange Servicu
        if (employee.getRole().getRoleName().equals(RoleName.ROLE_AGENT) || employee.getRole().getRoleName().equals(RoleName.ROLE_SUPERVISOR)){
            ExchangeEmployeeDto exchangeEmployeeDto = new ExchangeEmployeeDto();
            exchangeEmployeeDto.setEmployeeId(addedEmployee.getEmployeeId());
            exchangeEmployeeDto.setEmail(addedEmployee.getEmail());
            exchangeEmployeeDto.setRole(addedEmployee.getRole().getRoleName().toString());
            exchangeServiceClient.addActuary(exchangeEmployeeDto);
        }

        return EmployeeMapper.INSTANCE.employeeToEmployeeDto(addedEmployee);
    }

    public EmployeeDto updateEmployee(EmployeeUpdateDto employeeUpdateDto, Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("employee with" + id + " not found"));
        employee.setIsActive(true);
        EmployeeMapper.INSTANCE.updateEmployeeFromEmployeeUpdateDto(employee, employeeUpdateDto);
        return EmployeeMapper.INSTANCE.employeeToEmployeeDto(employeeRepository.save(employee));
    }

    public EmployeeDto deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("employee with" + id + " not found"));
        employee.setIsActive(false);
        return EmployeeMapper.INSTANCE.employeeToEmployeeDto(employeeRepository.save(employee));
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
        if (role.isEmpty()) {
            roleNameEnum = null;
        } else {
            roleNameEnum = RoleName.valueOf(role.toUpperCase());
        }
        List<Employee> employees = employeeRepository.findEmployees(firstName, lastName, email, roleNameEnum)
                .orElseThrow(() -> new NotFoundException("No employees found matching the criteria"));
        return employees.stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).collect(Collectors.toList());
    }


    public void setPassword(EmployeeSetPasswordDto setPasswordDto) {
        Employee employee = employeeRepository.findByEmail(setPasswordDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        employee.setPassword(passwordEncoder.encode(setPasswordDto.getPassword()));
        employeeRepository.save(employee);
    }

    //da se posalje spisak zaposlenih exchaneg servicu
    public List<ExchangeEmployeeDto> getExchangeEmployees() {
        Optional<List<Employee>> employees = employeeRepository.findSupervisorsAndAgents();
        List<ExchangeEmployeeDto> employeeDtos = new ArrayList<>();

        for(Employee employee : employees.get()){
            ExchangeEmployeeDto exchangeEmployeeDTO = new ExchangeEmployeeDto();
            exchangeEmployeeDTO.setEmployeeId(employee.getEmployeeId());
            exchangeEmployeeDTO.setEmail(employee.getEmail());
            exchangeEmployeeDTO.setRole(String.valueOf(employee.getRole().getRoleName()));
            employeeDtos.add(exchangeEmployeeDTO);
        }

        return employeeDtos;
    }
}
