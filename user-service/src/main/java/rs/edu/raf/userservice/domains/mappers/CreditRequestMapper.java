package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.dto.creditrequest.CreditRequestDto;
import rs.edu.raf.userservice.domains.model.Credit;
import rs.edu.raf.userservice.domains.model.CreditRequest;

public interface CreditRequestMapper {

    CreditRequestMapper INSTANCE = Mappers.getMapper(CreditRequestMapper.class);

}
