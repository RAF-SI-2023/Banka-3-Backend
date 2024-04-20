package rs.edu.raf.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.dto.contact.ContactCreateDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactUpdateDto;
import rs.edu.raf.userservice.domain.exception.NotFoundException;
import rs.edu.raf.userservice.domain.mapper.ContactMapper;
import rs.edu.raf.userservice.domain.model.Contact;
import rs.edu.raf.userservice.domain.model.User;
import rs.edu.raf.userservice.repository.ContactRepository;
import rs.edu.raf.userservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private ContactRepository contactRepository;
    private UserRepository userRepository;


    public List<ContactDto> findAll() {
        return contactRepository.findAll().stream().map(ContactMapper.INSTANCE::contactToContactDto)
                .collect(Collectors.toList());
    }

    public ContactDto findById(Long id) {
        return contactRepository.findById(id).map(ContactMapper.INSTANCE::contactToContactDto).orElseThrow();
    }

    public List<ContactDto> findByUserId(Long userId) {
        List<Contact> contacts = contactRepository.findByUser_UserId(userId).orElseThrow();
        return contacts.stream().map(ContactMapper.INSTANCE::contactToContactDto).collect(Collectors.toList());

    }

    public ContactDto createContact(ContactCreateDto contactCreateDto, Long userId) {
        Contact contact = ContactMapper.INSTANCE.contactCreateDtoToContact(contactCreateDto);
        User user = userRepository.findById(userId).orElseThrow();
        contact.setUser(user);
        contactRepository.save(contact);

        return ContactMapper.INSTANCE.contactToContactDto(contact);
    }

    public ContactDto updateContact(ContactUpdateDto contactUpdateDto, Long id) {
        Contact contact =
                contactRepository.findById(id).orElseThrow(() -> new NotFoundException("contact with" + id + " not " +
                        "found"));

        contact.setAccountNumber(contactUpdateDto.getAccountNumber());
        contact.setMyName(contactUpdateDto.getMyName());
        contact.setName(contactUpdateDto.getName());

        contactRepository.save(contact);
        return ContactMapper.INSTANCE.contactToContactDto(contact);

    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
