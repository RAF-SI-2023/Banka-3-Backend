package rs.edu.raf.userservice.domains.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.card.CardDto;
import rs.edu.raf.userservice.domains.model.Card;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    CardDto cardToCardDto(Card transaction);

    Card cardDtoToCard(CardDto transactionDto);
}
