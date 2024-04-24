package com.example.bankservice.domain.mapper;

import com.example.bankservice.domain.dto.card.CardDto;
import com.example.bankservice.domain.model.Card;
import org.mapstruct.Mapper;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public abstract class CardMapper {

    public abstract CardDto cardToCardDto(Card card);
}
