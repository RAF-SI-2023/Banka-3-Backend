package rs.edu.raf.userservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.userservice.domains.model.Contact;
import rs.edu.raf.userservice.domains.model.User;
import rs.edu.raf.userservice.repositories.ContactRepository;
import rs.edu.raf.userservice.services.ContactService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ContactServiceUnitTests {

    @Mock
    ContactRepository contactRepository;

    @Mock
    ContactService contactService;

    @Test
    public void testFindContactsByUserid(){

        Contact contact1 = createDummyContact("test1");
        Contact contact2 = createDummyContact("test2");
        Contact contact3 = createDummyContact("test3");

        User user = new User();
        user.setUserId(1L);


    }

    private Contact createDummyContact(String myName){
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setMyName(myName);
        contact.setName("Petar Petrovic");
        contact.setAccountNumber("700327520218530471");

        return contact;

    }


}
