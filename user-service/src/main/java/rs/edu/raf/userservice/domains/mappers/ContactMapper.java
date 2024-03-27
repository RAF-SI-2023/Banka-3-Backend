package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.contact.ContactCreateDto;
import rs.edu.raf.userservice.domains.dto.contact.ContactDto;
import rs.edu.raf.userservice.domains.model.Contact;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDto contactToContactDto(Contact contact);

    Contact contactCreateDtoToContact(ContactCreateDto contactCreateDto);
}
