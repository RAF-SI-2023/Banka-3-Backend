package rs.edu.raf.userservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import rs.edu.raf.userservice.domains.dto.employee.*;
import rs.edu.raf.userservice.domains.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UserDto;
import rs.edu.raf.userservice.domains.exceptions.ForbiddenException;
import rs.edu.raf.userservice.domains.exceptions.NotFoundException;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.Role;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.RoleName;
import rs.edu.raf.userservice.repositories.EmployeeRepository;
import rs.edu.raf.userservice.services.EmployeeService;
import rs.edu.raf.userservice.utils.EmailServiceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmailServiceClient emailServiceClient;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void createEmployeeTest(){
        EmployeeCreateDto employeeCreateDto = createDummyEmployeeCreateDto("pera123@gmail.com");
        Employee employee = createDummyEmployee("pera123@gmail.com");
        employee.setAddress(null);
        employee.setPassword(null);
        employee.setEmployeeId(null);
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        EmployeeDto employeeDto = employeeService.create(employeeCreateDto);

        assertEquals(employeeCreateDto.getFirstName(), employeeDto.getFirstName());
        assertEquals(employeeCreateDto.getLastName(), employeeDto.getLastName());
        assertEquals(employeeCreateDto.getEmail(), employeeDto.getEmail());
        assertEquals(employeeCreateDto.getJmbg(), employeeDto.getJmbg());
    }


    @Test
    public void deleteEmployeeTest(){
        Employee employee = createDummyEmployee("employee123@gmail.com");

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);
        EmployeeDto employeeDto = employeeService.delete(1L);

        assertEquals(employee.getEmail(), employeeDto.getEmail());
        assertEquals(employee.getJmbg(), employeeDto.getJmbg());
        assertFalse(employeeDto.getIsActive());

    }
    @Test
    public void updateEmployeeTest(){
        Employee employee = createDummyEmployee("employee123@gmail.com");
        EmployeeUpdateDto employeeUpdateDto = createDummyEmployeeUpdateDto();
        employeeUpdateDto.setFirstName("Mika");
        employeeUpdateDto.setLastName("Mikic");

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);

        EmployeeDto employeeDto = employeeService.update(employeeUpdateDto, 1L);

        assertEquals(employeeUpdateDto.getEmail(), employeeDto.getEmail());
        assertEquals(employeeUpdateDto.getFirstName(), employeeDto.getFirstName());
        assertEquals(employeeUpdateDto.getLastName(), employeeDto.getLastName());

    }

    @Test
    public void updateEmployeeTest_Fail(){
        EmployeeUpdateDto employeeUpdateDto = createDummyEmployeeUpdateDto();
        employeeUpdateDto.setIsActive(false);
        Employee employee = createDummyEmployee("employee123@gmail.com");
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));


        assertNull(employeeService.update(employeeUpdateDto, 1L));
    }

    @Test
    public void findAllEmployeeTest(){
        Employee employee1 = createDummyEmployee("employee1@gmail.com");
        Employee employee2 = createDummyEmployee("employee2@gmail.com");
        employee2.setFirstName("Mika");
        employee2.setLastName("Mikic");
        employee2.setJmbg("0987654321098");

        List<Employee> employees = List.of(employee1, employee2);

        given(employeeRepository.findAll()).willReturn(employees);

        List<EmployeeDto> userDtos = employeeService.findAll();

        for (EmployeeDto edto : userDtos) {
            boolean found = false;
            for (Employee e : employees) {
                if (edto.getEmail().equals(e.getEmail()) &&
                        edto.getJmbg().equals(e.getJmbg())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Employee not found");
            }
        }
    }
    @Test
    public void findByIdTest_NotFound(){
        given(employeeRepository.findById(1L)).willReturn(null);

        assertThrows(Exception.class, () -> employeeService.findById(1L));

    }
    @Test
    public void findByEmailTest() {
        Employee employee = createDummyEmployee("employee123@gmail.com");

        given(employeeRepository.findByEmail("employee123@gmail.com")).willReturn(Optional.of(employee));
        EmployeeDto employeeDto = employeeService.findByEmail("employee123@gmail.com");
        assertEquals(employee.getEmail(), employeeDto.getEmail());
        assertEquals(employee.getJmbg(), employeeDto.getJmbg());
    }
    @Test
    public void findByUsernameTest() {
        Employee employee = createDummyEmployee("employee123@gmail.com");

        given(employeeRepository.findByUsername("perica")).willReturn(Optional.of(employee));
        EmployeeDto employeeDto = employeeService.findByUsername("perica");
        assertEquals(employee.getUsername(), employeeDto.getUsername());
        assertEquals(employee.getJmbg(), employeeDto.getJmbg());
    }

    @Test
    public void findByMobileNumberTest_Fail() {

        given(employeeRepository.findByPhoneNumber("+3123214254")).willReturn(null);

        assertThrows(NullPointerException.class, () -> employeeService.findByMobileNumber("+3123214254"));
    }

    @Test
    public void searchTest() {
        Employee employee1 = createDummyEmployee("employee1@gmail.com");
        Employee employee2 = createDummyEmployee("employee2@gmail.com");

        List<Employee> employees = List.of(employee1, employee2);
        given(employeeRepository.findEmployees("Pera","Peric",null, RoleName.ROLE_BANKING_OFFICER)).willReturn(Optional.of(employees));

        List<EmployeeDto> employeeDtos = employeeService.search("Pera","Peric",null,"ROLE_BANKING_OFFICER");

        for (EmployeeDto edto : employeeDtos) {
            boolean found = false;
            for (Employee e : employees) {
                if (edto.getEmail().equals(e.getEmail()) && edto.getJmbg().equals(e.getJmbg())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Employee not found");
            }
        }

    }
    @Test
    public void loadUserByUsername() {
        Employee employee = createDummyEmployee("pera123@gmail.com");
        Role role = new Role();
        role.setRoleName(RoleName.ROLE_BANKING_OFFICER);
        employee.setRole(role);
        given(employeeRepository.findByEmail("pera123@gmail.com")).willReturn(Optional.of(employee));

        UserDetails userDetails = employeeService.loadUserByUsername("pera123@gmail.com");
        assertEquals(employee.getEmail(), userDetails.getUsername());
        assertEquals(employee.getPassword(), userDetails.getPassword());
    }

    @Test
    public void loadUserByUsername_Not_Active() {
        Employee employee = createDummyEmployee("employee@gmail.com");
        employee.setIsActive(false);
        given(employeeRepository.findByEmail("employee@gmail.com")).willReturn(Optional.of(employee));

        assertThrows(ForbiddenException.class, ()-> employeeService.loadUserByUsername("employee@gmail.com"));
    }
    @Test
    public void setPasswordTest() {
        Employee employee = createDummyEmployee("employee@gmail.com");
        SetPasswordDTO setPasswordDTO = new SetPasswordDTO("pera1234", "employee@gmail.com");

        given(employeeRepository.findByEmail("employee@gmail.com")).willReturn(Optional.of(employee));
        given(employeeRepository.save(employee)).willReturn(employee);
        when(passwordEncoder.encode("pera1234")).thenReturn("encodedPassword");

        //TODO DA VIDIMO DAL TREBA DA SE PROVERI DAL JE PASSWORD ISTI
        String result = employeeService.setPassword(setPasswordDTO);
        assertEquals("Successfully updated password for " + setPasswordDTO.getEmail(), result);
    }
    @Test
    public void setPasswordTest_Fail() {
        given(employeeRepository.findByEmail("email@gmail.com")).willReturn(Optional.empty());
        SetPasswordDTO setPasswordDTO = new SetPasswordDTO("pera1234", "email@gmail.com");
        assertThrows(ResponseStatusException.class, () -> employeeService.setPassword(setPasswordDTO));
    }

    @Test
    public void resetPasswordTest() {
        Employee employee = createDummyEmployee("pera123@gmail.com");
        given(employeeRepository.findByEmail("pera123@gmail.com")).willReturn(Optional.of(employee));

        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setEmail("pera123@gmail.com");
        resetPasswordDTO.setNewPassword("pera1234");

        when(passwordEncoder.encode("pera1234")).thenReturn("encodedPassword");

        String result = employeeService.resetPassword(resetPasswordDTO);
        assertEquals("Successfully reseted password for " + resetPasswordDTO.getEmail(), result);
    }


    private Employee createDummyEmployee(String email) {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("Pera");
        employee.setLastName("Peric");
        employee.setUsername("perica");
        employee.setJmbg("1234567890123");
        employee.setDateOfBirth(123L);
        employee.setGender("M");
        employee.setPhoneNumber("+3123214254");
        employee.setEmail(email);
        employee.setPassword("pera1234");
        employee.setIsActive(true);
        employee.setAddress("Mika Mikic 13");
        employee.setPermissions(new ArrayList<>()); //zbog metode loadUserByUsername

        return employee;
    }

    private EmployeeUpdateDto createDummyEmployeeUpdateDto() {
        EmployeeUpdateDto employee = new EmployeeUpdateDto();
        employee.setFirstName("Pera");
        employee.setLastName("Peric");
        employee.setUsername("perkan");
        employee.setEmail("pera123@gmail.com");
        employee.setDateOfBirth(123L);
        employee.setGender("M");
        employee.setJmbg("1234567890123");
        employee.setAddress("Mika Mikic 13");
        employee.setPhoneNumber("+3123214254");
        employee.setIsActive(true);

        return employee;
    }
    private EmployeeCreateDto createDummyEmployeeCreateDto(String email) {

        EmployeeCreateDto employee = new EmployeeCreateDto();
        employee.setFirstName("Pera");
        employee.setLastName("Peric");
        employee.setUsername("perkan");
        employee.setEmail(email);
        employee.setDateOfBirth(123L);
        employee.setGender("M");
        employee.setJmbg("1234567890123");
        employee.setAddress("Mika Mikic 13");
        employee.setPhoneNumber("+3123214254");

        return employee;
    }





}
