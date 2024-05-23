package rs.edu.raf.exchangeservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.exchangeservice.domain.dto.myListing.MyOptionDto;
import rs.edu.raf.exchangeservice.domain.model.myListing.MyOption;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface MyOptionMapper {

    MyOptionMapper INSTANCE = Mappers.getMapper(MyOptionMapper.class);

    MyOptionDto myOptionToMyOptionDto(MyOption myOption);
}
