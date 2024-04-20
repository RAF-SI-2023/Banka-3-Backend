package rs.edu.raf.userservice.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domain.dto.employee.EmployeeCreateDto;
import rs.edu.raf.userservice.domain.dto.employee.EmployeeDto;
import rs.edu.raf.userservice.domain.dto.employee.EmployeeUpdateDto;
import rs.edu.raf.userservice.domain.model.Employee;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDto employeeToEmployeeDto(Employee employee);

    Employee employeeCreateDtoToEmployee(EmployeeCreateDto employeeCreateDto);

    void updateEmployeeFromEmployeeUpdateDto(@MappingTarget Employee employee, EmployeeUpdateDto employeeUpdateDto);
}
