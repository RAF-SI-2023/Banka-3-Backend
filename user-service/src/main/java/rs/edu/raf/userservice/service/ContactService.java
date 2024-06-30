package rs.edu.raf.userservice.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.userservice.domain.dto.contact.ContactPostPutDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.exception.NotFoundException;
import rs.edu.raf.userservice.domain.mapper.ContactMapper;
import rs.edu.raf.userservice.domain.model.Contact;
import rs.edu.raf.userservice.domain.model.User;
import rs.edu.raf.userservice.repository.ContactRepository;
import rs.edu.raf.userservice.repository.UserRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    private Counter listUsers = null;
    private AtomicInteger randomInt;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository, CompositeMeterRegistry meterRegistry) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;

        this.listUsers = meterRegistry.counter("contact.getByUser");
        this.randomInt = meterRegistry.gauge("contact.gauge", new AtomicInteger(0));
    }

    public List<ContactDto> findByUserId(Long userId) {
        List<Contact> contacts = contactRepository.findByUser_UserId(userId).orElseThrow();
        return contacts.stream().map(ContactMapper.INSTANCE::contactToContactDto).collect(Collectors.toList());
    }

    public ContactDto createContact(ContactPostPutDto contactCreateDto, Long userId) {
        Contact contact = ContactMapper.INSTANCE.contactPostPutDtoToContact(contactCreateDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user with " + userId + " not found"));
        contact.setUser(user);
        contactRepository.save(contact);

        return ContactMapper.INSTANCE.contactToContactDto(contact);
    }

    public ContactDto updateContact(ContactPostPutDto contactPostPutDto, Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new NotFoundException("contact with" + id + " not found"));

        contact.setAccountNumber(contactPostPutDto.getAccountNumber());
        contact.setMyName(contactPostPutDto.getMyName());
        contact.setName(contactPostPutDto.getName());

        contactRepository.save(contact);
        return ContactMapper.INSTANCE.contactToContactDto(contact);
    }

    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
        contactRepository.delete(contact);
    }
}
