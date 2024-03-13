package rs.edu.raf.userservice;

import io.cucumber.java.an.E;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.dto.EmployeeDto;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.repositories.EmployeeRepository;
import rs.edu.raf.userservice.services.EmployeeService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;


    @Test
    void findByEmail(){
        Employee employee = new Employee();
        employee.setEmail("email@gmail.com");
        given(employeeRepository.save(employee)).willReturn(employee);

        try{
            EmployeeDto employeeDto = employeeService.findByEmail("email@gmail.com");
            assertNotNull(employeeDto);
        }catch (Exception e){
            fail();
        }
    }



}
