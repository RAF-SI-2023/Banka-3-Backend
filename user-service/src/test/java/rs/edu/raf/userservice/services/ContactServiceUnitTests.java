package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactPostPutDto;
import rs.edu.raf.userservice.domain.mapper.ContactMapper;
import rs.edu.raf.userservice.domain.model.Contact;
import rs.edu.raf.userservice.domain.model.User;
import rs.edu.raf.userservice.repository.ContactRepository;
import rs.edu.raf.userservice.repository.UserRepository;
import rs.edu.raf.userservice.service.ContactService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceUnitTests {

    @Mock
    private ContactRepository contactRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ContactService contactService;


    @Test
    public void testFindByUserId() {
        List<Contact> contacts = createDummyContacts(1L);
        List<ContactDto> expectedDtos = contacts.stream().map(ContactMapper.INSTANCE::contactToContactDto).collect(Collectors.toList());

        when(contactRepository.findByUser_UserId(1L)).thenReturn(Optional.of(contacts));

        List<ContactDto> result = contactService.findByUserId(1L);

        assertEquals(expectedDtos, result);
    }
    @Test
    public void testFindByUserId_ContactsNotFound() {

        given(contactRepository.findByUser_UserId(1l)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contactService.findByUserId(1l));
    }
    @Test
    public void testCreateContact() {
        Long userId = 1L;
        ContactPostPutDto contactCreateDto = new ContactPostPutDto();
        contactCreateDto.setMyName("Test Contact");

        User user = new User();
        user.setUserId(userId);

        Contact contact = ContactMapper.INSTANCE.contactPostPutDtoToContact(contactCreateDto);
        contact.setUser(user);

        ContactDto expectedDto = ContactMapper.INSTANCE.contactToContactDto(contact);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        ContactDto result = contactService.createContact(contactCreateDto, userId);

        assertEquals(expectedDto, result);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }
    @Test
    public void testUpdateContact() {
        Long contactId = 1L;
        ContactPostPutDto updateDto = new ContactPostPutDto();
        updateDto.setAccountNumber("1234567890");
        updateDto.setMyName("Updated Name");
        updateDto.setName("Updated Contact");

        Contact existingContact = new Contact();
        existingContact.setContactId(contactId);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(existingContact);

        ContactDto updatedContactDto = contactService.updateContact(updateDto, contactId);

        assertEquals(updateDto.getAccountNumber(), updatedContactDto.getAccountNumber());
        assertEquals(updateDto.getMyName(), updatedContactDto.getMyName());
        assertEquals(updateDto.getName(), updatedContactDto.getName());
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    public void testUpdateContactNotFound() {
        Long contactId = 1L;
        ContactPostPutDto updateDto = new ContactPostPutDto();
        updateDto.setAccountNumber("1234567890");
        updateDto.setMyName("Updated Name");
        updateDto.setName("Updated Contact");

        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> contactService.updateContact(updateDto, contactId));

    }

    @Test
    public void testDeleteContact() {
        Long id = 1L;
        Contact contact = new Contact();
        contact.setContactId(id);
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        contactService.deleteContact(id);

        verify(contactRepository, times(1)).findById(id);
        verify(contactRepository, times(1)).delete(contact);
    }

    private Contact createDummyContact(String myName){
        Contact contact = new Contact();
        contact.setContactId(1L);
        contact.setMyName(myName);
        contact.setName("Petar Petrovic");
        contact.setAccountNumber("700327520218530471");

        return contact;

    }
    private List<Contact> createDummyContacts(Long userId) {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Contact contact = new Contact();
            contact.setContactId((long) i);
            contact.setMyName("Contact " + i);
            contact.setName("Petar Petrovic");
            contact.setAccountNumber("700327520218530471");
            contacts.add(contact);
        }
        return contacts;
    }


}
