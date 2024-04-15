package rs.edu.raf.exchangeservice.domain.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.exchangeservice.domain.dto.OptionOrderDto;
import rs.edu.raf.exchangeservice.domain.dto.StockOrderDto;
import rs.edu.raf.exchangeservice.domain.model.order.OptionOrder;
import rs.edu.raf.exchangeservice.domain.model.order.StockOrder;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OptionMapper {

    OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);

    OptionOrderDto optionOrderToOptionOrderDto(OptionOrder optionOrder);
}