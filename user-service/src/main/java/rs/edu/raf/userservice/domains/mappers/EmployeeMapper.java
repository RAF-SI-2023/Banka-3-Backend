package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeCreateDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeUpdateDto;
import rs.edu.raf.userservice.domains.model.Employee;
import rs.edu.raf.userservice.domains.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDto employeeToEmployeeDto(Employee employee);

    Employee employeeCreateDtoToEmployee(EmployeeCreateDto employeeCreateDto);

    void updateEmployeeFromEmployeeUpdateDto(@MappingTarget Employee employee, EmployeeUpdateDto employeeUpdateDto);
}
