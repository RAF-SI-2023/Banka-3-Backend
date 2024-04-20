package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.mappers.ContactMapper;
import rs.edu.raf.userservice.domain.model.Contact;
import rs.edu.raf.userservice.domain.model.User;
import rs.edu.raf.userservice.repositories.ContactRepository;
import rs.edu.raf.userservice.repositories.UserRepository;
import rs.edu.raf.userservice.services.ContactService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
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
    public void testFindAll() {
        // Priprema podataka za test
        List<Contact> contacts = Arrays.asList(
                createDummyContact("test1"),
                createDummyContact("test2")
        );
        List<ContactDto> expectedDtos = contacts.stream()
                .map(ContactMapper.INSTANCE::contactToContactDto)
                .toList();

        // Podešavanje ponašanja mock ContactRepository-ja
        when(contactRepository.findAll()).thenReturn(contacts);

        // Izvršavanje metode koju testiramo
        List<ContactDto> result = contactService.findAll();

        // Provera rezultata
        assertEquals(expectedDtos.size(), result.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            assertEquals(expectedDtos.get(i), result.get(i));
        }
        // Provera da li je findAll metoda pozvana tačno jednom
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_ContactFound() {
        // Priprema podataka za test

        Contact contact = createDummyContact("test1");
        ContactDto expectedDto = ContactMapper.INSTANCE.contactToContactDto(contact);

        // Podešavanje ponašanja mock ContactRepository-ja
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));

        // Izvršavanje metode koju testiramo
        ContactDto result = contactService.findById(contact.getId());

        // Provera rezultata
        assertEquals(expectedDto, result);
        // Provera da li je findById metoda pozvana tačno jednom sa zadatim contactId
        verify(contactRepository, times(1)).findById(contact.getId());
    }

    @Test
    public void testFindById_ContactNotFound() {
        // Priprema podataka za test
        Contact contact = createDummyContact("test1");

        // Provera da li se izuzetak baca prilikom izvršavanja metode
        assertThrows(RuntimeException.class, () -> contactService.findByUserId(contact.getId()));
    }
    @Test
    public void testFindByUserId_ContactsFound() {
        // Priprema podataka za test
        Long userId = 1L;

        List<Contact> contacts = createDummyContacts(userId);
        List<ContactDto> expectedDtos = contacts.stream().map(ContactMapper.INSTANCE::contactToContactDto).collect(Collectors.toList());

        // Podešavanje ponašanja mock ContactRepository-ja
        when(contactRepository.findByUser_UserId(userId)).thenReturn(Optional.of(contacts));

        // Izvršavanje metode koju testiramo
        List<ContactDto> result = contactService.findByUserId(userId);

        // Provera rezultata
        assertEquals(expectedDtos, result);
        // Provera da li je findByUserId metoda pozvana tačno jednom sa zadatim userId
        verify(contactRepository, times(1)).findByUser_UserId(userId);
    }

    @Test
    public void testFindByUserId_ContactsNotFound() {
        // Priprema podataka za test
        Contact contact = createDummyContact("test1");

        // Izvršavanje metode koju testiramo - očekuje se da izbaci RuntimeException
        assertThrows(RuntimeException.class, () -> contactService.findByUserId(contact.getUser().getUserId()));
    }
    @Test
    public void testCreateContact() {
        // Priprema podataka za test
        Long userId = 1L;
        ContactCreateDto contactCreateDto = new ContactCreateDto();
        contactCreateDto.setMyName("Test Contact");
        // Postavite ostale potrebne atribute contactCreateDto objekta

        User user = new User();
        user.setUserId(userId);
        // Postavite ostale potrebne atribute User objekta

        Contact contact = ContactMapper.INSTANCE.contactCreateDtoToContact(contactCreateDto);
        contact.setUser(user);

        ContactDto expectedDto = ContactMapper.INSTANCE.contactToContactDto(contact);

        // Podešavanje ponašanja mock UserRepository-ja
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // Podešavanje ponašanja mock ContactRepository-ja
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);

        // Izvršavanje metode koju testiramo
        ContactDto result = contactService.createContact(contactCreateDto, userId);

        // Provera rezultata
        assertEquals(expectedDto, result);
        // Provera da li je metoda save pozvana nad mock ContactRepository-jem
        verify(contactRepository, times(1)).save(any(Contact.class));
    }
    @Test
    public void testUpdateContact() {
        // Priprema podataka za test
        Long contactId = 1L;
        ContactUpdateDto updateDto = new ContactUpdateDto();
        updateDto.setAccountNumber("1234567890");
        updateDto.setMyName("Updated Name");
        updateDto.setName("Updated Contact");

        Contact existingContact = new Contact();
        existingContact.setId(contactId);
        // Postavite ostale potrebne atribute existingContact objekta

        // Podešavanje ponašanja mock ContactRepository-ja
        when(contactRepository.findById(contactId)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(existingContact);

        // Izvršavanje metode koju testiramo
        ContactDto updatedContactDto = contactService.updateContact(updateDto, contactId);

        // Provera rezultata
        assertEquals(updateDto.getAccountNumber(), updatedContactDto.getAccountNumber());
        assertEquals(updateDto.getMyName(), updatedContactDto.getMyName());
        assertEquals(updateDto.getName(), updatedContactDto.getName());
        // Provera da li je metoda save pozvana nad mock ContactRepository-jem
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    public void testUpdateContactNotFound() {
        // Priprema podataka za test
        Long contactId = 1L;
        ContactUpdateDto updateDto = new ContactUpdateDto();
        updateDto.setAccountNumber("1234567890");
        updateDto.setMyName("Updated Name");
        updateDto.setName("Updated Contact");

        // Podešavanje ponašanja mock ContactRepository-ja da ne vrati postojeći kontakt
        //when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        // Izvršavanje metode koju testiramo, očekujemo da baci izuzetak NotFoundException
        //contactService.updateContact(updateDto, contactId);
        assertThrows(RuntimeException.class, () -> contactService.updateContact(updateDto, contactId));

    }

    @Test
    public void testDeleteContact() {
        // Priprema podataka za test
        Contact contact = createDummyContact("test1");

        // Izvršavanje metode koju testiramo
        contactService.deleteContact(contact.getId());

        // Provera da li je metoda deleteById pozvana nad mock ContactRepository-jem sa ispravnim ID-em
        verify(contactRepository, times(1)).deleteById(contact.getId());
    }

    private Contact createDummyContact(String myName){
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setMyName(myName);
        contact.setName("Petar Petrovic");
        contact.setAccountNumber("700327520218530471");

        return contact;

    }
    private List<Contact> createDummyContacts(Long userId) {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Contact contact = new Contact();
            contact.setId((long) i);
            contact.setMyName("Contact " + i);
            contact.setName("Petar Petrovic");
            contact.setAccountNumber("700327520218530471");
            contacts.add(contact);
        }
        return contacts;
    }


}
