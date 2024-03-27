package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestCreateDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestDto;
import rs.edu.raf.userservice.domains.model.CreditRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreditRequestMapper {

    CreditRequestMapper INSTANCE = Mappers.getMapper(CreditRequestMapper.class);

    CreditRequestDto creditRequestToCreditRequestDto(CreditRequest creditRequest);

    CreditRequest creditRequestCreateDtoToCreditRequest(CreditRequestCreateDto creditRequestDto);
}
