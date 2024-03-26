package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.model.Credit;

public interface CreditMapper {

    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    CreditDto creditToCreditDto(Credit credit);

    Credit createCreditDtoToCredit(CreateCreditDto createCreditDto);
}
