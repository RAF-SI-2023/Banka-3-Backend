package rs.edu.raf.userservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeUpdateDto;
import rs.edu.raf.userservice.domains.dto.user.CreateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UpdateUserDto;
import rs.edu.raf.userservice.domains.dto.user.UserDto;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.domains.model.enums.RoleName;
import rs.edu.raf.userservice.repositories.EmployeeRepository;
import rs.edu.raf.userservice.services.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void createEmployeeTest(){
        EmployeeCreateDto employeeCreateDto = createDummyEmployeeCreateDto("employee123@gmail.com");
        Employee employee = createDummyEmployee("employee123@gmail.com");
        employee.setAddress(null);
        employee.setPassword(null);
        employee.setEmployeeId(null);
        //given(employeeRepository.save(employee)).willReturn(employee);
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        EmployeeDto userDto = employeeService.create(employeeCreateDto);

        assertEquals(userDto.getFirstName(), employee.getFirstName());
        assertEquals(userDto.getLastName(), employee.getLastName());
        assertEquals(userDto.getEmail(), employee.getEmail());
        assertEquals(userDto.getJmbg(), employee.getJmbg());
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

        given(employeeRepository.findByEmail("employee123@gmail.com")).willReturn(Optional.of(employee));
        EmployeeDto employeeDto = employeeService.findByEmail("employee123@gmail.com");
        assertEquals(employee.getUsername(), employeeDto.getUsername());
        assertEquals(employee.getJmbg(), employeeDto.getJmbg());
    }

    @Test
    public void findByMobileNumberTest_Fail() {

        given(employeeRepository.findByPhoneNumber("+3123214254")).willReturn(null);

        assertThrows(NullPointerException.class, () -> employeeService.findByMobileNumber("+3123214254"));
    }

    @Test
    public void findByPositionTest() {
        String position="pos";
        Employee employee1 = createDummyEmployee("employee1@gmail.com");
        Employee employee2 = createDummyEmployee("employee2@gmail.com");
        employee1.setPosition(position);
        employee2.setPosition(position);

        List<Employee> employees = List.of(employee1, employee2);
        given(employeeRepository.findByPosition("pos")).willReturn(employees);

        List<EmployeeDto> userDtos = employeeService.findByPosition(position);

        for (EmployeeDto edto : userDtos) {
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
    public void searchTest() {
        Employee employee1 = createDummyEmployee("employee1@gmail.com");
        Employee employee2 = createDummyEmployee("employee2@gmail.com");

        List<Employee> employees = List.of(employee1, employee2);
        given(employeeRepository.findEmployees("Pera","Peric",null, RoleName.EMPLOYEE)).willReturn(Optional.of(employees));

        List<EmployeeDto> employeeDtos = employeeService.search("Pera","Peric",null,"EMPLOYEE");

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
        Employee employee = createDummyEmployee("employee@gmail.com");

        given(employeeRepository.findByEmail("employee@gmail.com")).willReturn(Optional.of(employee));

        UserDetails userDetails = employeeService.loadUserByUsername("employee@gmail.com");

        assertEquals(userDetails.getUsername(), employee.getEmail());
        assertEquals(userDetails.getPassword(), employee.getPassword());
    }




    private Employee createDummyEmployee(String email) {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("Pera");
        employee.setLastName("Peric");
        employee.setJmbg("1234567890123");
        employee.setDateOfBirth(123L);
        employee.setGender("M");
        employee.setPhoneNumber("+3123214254");
        employee.setEmail(email);
        employee.setPassword("pera1234");
        employee.setIsActive(true);
        employee.setAddress("Mika Mikic 13");
        employee.setPosition("Pozicija");
        employee.setDepartment("Department");
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
        employee.setDepartment("Department");
        employee.setPosition("Pozicija");
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
        employee.setDepartment("Department");
        employee.setPosition("Pozicija");
        employee.setPhoneNumber("+3123214254");

        return employee;
    }





}
