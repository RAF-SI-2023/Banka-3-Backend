package rs.edu.raf.userservice.domains.mappers;

import org.mapstruct.factory.Mappers;
import rs.edu.raf.userservice.domains.dto.contact.ContactCreateDto;
import rs.edu.raf.userservice.domains.dto.contact.ContactDto;
import rs.edu.raf.userservice.domains.model.Contact;

public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDto contactToContactDto(Contact contact);

    Contact contactCreateDtoToContact(ContactCreateDto contactCreateDto);
}
