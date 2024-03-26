package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.contact.ContactCreateDto;
import rs.edu.raf.userservice.domains.dto.contact.ContactDto;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountDto;
import rs.edu.raf.userservice.services.ContactService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/contact")
public class ContactController {

    private ContactService contactService;

    @GetMapping("/getAll")
    public List<ContactDto> getAll() {
        return contactService.findAll();
    }

    @GetMapping(path = "/{userId}")
    public List<ContactDto> getByUser(Long userId) {
        return contactService.findByUserId(userId);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ContactDto createContact(@RequestBody ContactCreateDto contactCreateDto) {
        return contactService.createContact(contactCreateDto);
    }

    @DeleteMapping("/{contactId}")
    public void deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
    }
}
