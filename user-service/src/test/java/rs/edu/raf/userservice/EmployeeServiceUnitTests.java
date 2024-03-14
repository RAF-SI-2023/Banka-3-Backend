package rs.edu.raf.userservice;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.repositories.EmployeeRepository;
import rs.edu.raf.userservice.services.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;





}
