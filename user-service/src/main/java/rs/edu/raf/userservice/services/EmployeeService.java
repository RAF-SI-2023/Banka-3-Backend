package rs.edu.raf.userservice.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domains.dto.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.EmployeeUpdateDto;
import rs.edu.raf.userservice.domains.exceptions.NotFoundException;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
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

        Employee employee = new Employee();

        employee.setFirstName(employeeCreateDto.getFirstName());
        employee.setLastName(employeeCreateDto.getLastName());
        employee.setUsername(employeeCreateDto.getUsername());
        employee.setJmbg(employeeCreateDto.getJmbg());
        employee.setEmail(employeeCreateDto.getEmail());
        employee.setDateOfBirth(employeeCreateDto.getDateOfBirth());
        employee.setGender(employeeCreateDto.getGender());
        employee.setAddress(employeeCreateDto.getAddress());
        employee.setDepartment(employeeCreateDto.getDepartment());
        employee.setPosition(employeeCreateDto.getPosition());
        employee.setPhoneNumber(employeeCreateDto.getPhoneNumber());
        employee.setIsActive(true);
        employee.setRoles(employeeCreateDto.getRoles());

        employee.setPassword(employeeCreateDto.getPassword());
        employee.setSaltPassword(passwordEncoder.encode(employee.getPassword()));

        employeeRepository.save(employee);
        return convertEmployeeToDto(employee);
    }

    public EmployeeDto delete(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("employee not found"));
        employee.setIsActive(false);
        employeeRepository.save(employee);
        return convertEmployeeToDto(employee);
    }

    public EmployeeDto update(EmployeeUpdateDto employeeUpdateDto, Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));

        employee.setIsActive(employeeUpdateDto.getIsActive());
        if (!employee.getIsActive()) {
            return null;
        }
        employee.setFirstName(employeeUpdateDto.getFirstName());
        employee.setLastName(employeeUpdateDto.getLastName());
        employee.setUsername(employeeUpdateDto.getUsername());
        employee.setJmbg(employeeUpdateDto.getJmbg());
        employee.setEmail(employeeUpdateDto.getEmail());
        employee.setDateOfBirth(employeeUpdateDto.getDateOfBirth());
        employee.setGender(employeeUpdateDto.getGender());
        employee.setAddress(employeeUpdateDto.getAddress());
        employee.setDepartment(employeeUpdateDto.getDepartment());
        employee.setPosition(employeeUpdateDto.getPosition());
        employee.setPhoneNumber(employeeUpdateDto.getPhoneNumber());//ako nije aktivan ne moze update
        employee.setRoles(employeeUpdateDto.getRoles());

        if (!employee.getPassword().equals(employeeUpdateDto.getPassword())) {
            employee.setPassword(employeeUpdateDto.getPassword());
            employee.setSaltPassword(passwordEncoder.encode(employee.getPassword()));
        }

        employeeRepository.save(employee);
        return convertEmployeeToDto(employee);
    }

    public List<EmployeeDto> findAll() {
//        List<EmployeeDto> dtos;

        return employeeRepository.findAll().stream().map(this::convertEmployeeToDto).collect(Collectors.toList());
    }

    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
        return convertEmployeeToDto(employee);
    }

    public EmployeeDto findByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("user not found"));
        return convertEmployeeToDto(employee);
    }

    public EmployeeDto findByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
        return convertEmployeeToDto(employee);
    }

    public EmployeeDto findByMobileNumber(String mobileNumber) {
        Employee employee = employeeRepository.findByPhoneNumber(mobileNumber).orElseThrow(() -> new NotFoundException("user not found"));
        return convertEmployeeToDto(employee);
    }

    public EmployeeDto findByJmbg(String jmbg) {
        Employee employee = employeeRepository.findByJmbg(jmbg).orElseThrow(() -> new NotFoundException("user not found"));
        return convertEmployeeToDto(employee);
    }

    public List<EmployeeDto> findByPosition(String position) {
        return employeeRepository.findByPosition(position).stream().map(this::convertEmployeeToDto).collect(Collectors.toList());
    }

    private EmployeeDto convertEmployeeToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();

        dto.setEmployeeId(employee.getEmployeeId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setUsername(employee.getUsername());
        dto.setJmbg(employee.getJmbg());
        dto.setEmail(employee.getEmail());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setGender(employee.getGender());
        dto.setAddress(employee.getAddress());
        dto.setDepartment(employee.getDepartment());
        dto.setPosition(employee.getPosition());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setIsActive(employee.getIsActive());
        dto.setRole(employee.getRoles());

        dto.setPassword(employee.getPassword());

        return dto;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee user = this.employeeRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("user not found"));

        if (user == null) {
            throw new UsernameNotFoundException("User with the email: " + email + " not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
