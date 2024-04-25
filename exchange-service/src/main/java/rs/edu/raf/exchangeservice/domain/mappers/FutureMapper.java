package rs.edu.raf.exchangeservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.exchangeservice.domain.dto.listing.FutureDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface FutureMapper {

    FutureMapper INSTANCE = Mappers.getMapper(FutureMapper.class);

     FutureDto futureToFutureDto(Future stock);
}
