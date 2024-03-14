package rs.edu.raf.userservice.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeUpdateDto;
import rs.edu.raf.userservice.domains.exceptions.ForbiddenException;
import rs.edu.raf.userservice.domains.exceptions.NotFoundException;
import rs.edu.raf.userservice.domains.mappers.EmployeeMapper;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.Permission;
import rs.edu.raf.userservice.domains.model.enums.RoleName;
import rs.edu.raf.userservice.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public EmployeeDto create(EmployeeCreateDto employeeCreateDto) {

        Employee employee = EmployeeMapper.INSTANCE.employeeCreateDtoToEmployee(employeeCreateDto);
        employee.setIsActive(true);
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

    public EmployeeDto findByMobileNumber(String mobileNumber) {
        Optional<Employee> employee = employeeRepository.findByPhoneNumber(mobileNumber);
        return employee.map(EmployeeMapper.INSTANCE::employeeToEmployeeDto).orElseThrow(() -> new NotFoundException(
                "user with" + mobileNumber + " not found"));
    }

    public List<EmployeeDto> findByPosition(String position) {
        return employeeRepository.findByPosition(position).stream().map(EmployeeMapper.INSTANCE::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDto> search(String firstName, String lastName, String email, String role) {
        RoleName roleNameEnum;
        try {
            if(role.isEmpty()){
                roleNameEnum = null;
            }else{
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

        return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(),
                authorities);
    }
}
