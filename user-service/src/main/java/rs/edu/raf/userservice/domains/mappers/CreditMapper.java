package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.model.Credit;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreditMapper {

    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    CreditDto creditToCreditDto(Credit credit);

    Credit createCreditDtoToCredit(CreateCreditDto createCreditDto);
}
