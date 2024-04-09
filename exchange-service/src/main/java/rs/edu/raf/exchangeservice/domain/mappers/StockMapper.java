package rs.edu.raf.exchangeservice.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.exchangeservice.domain.dto.StockDto;
import rs.edu.raf.exchangeservice.domain.model.listing.Stock;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockDto stockToStockDto(Stock stock);
}
