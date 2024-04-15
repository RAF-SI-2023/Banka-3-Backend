package rs.edu.raf.exchangeservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.exchangeservice.domain.dto.FutureDto;
import rs.edu.raf.exchangeservice.domain.dto.StockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Future;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface FutureMapper {

    FutureMapper INSTANCE = Mappers.getMapper(FutureMapper.class);

     FutureDto futureToFutureDto(Future stock);
}
