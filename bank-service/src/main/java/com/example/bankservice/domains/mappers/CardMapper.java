package com.example.bankservice.domains.mappers;


import com.example.bankservice.domains.dto.CardDto;
import com.example.bankservice.domains.dto.CreditTransactionDto;
import com.example.bankservice.domains.dto.TransactionDto;
import com.example.bankservice.domains.model.Card;
import com.example.bankservice.domains.model.CreditTransaction;
import com.example.bankservice.domains.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

    CardDto cardToCardDto(Card transaction);

    Card cardDtoToCard(CardDto transactionDto);
}
