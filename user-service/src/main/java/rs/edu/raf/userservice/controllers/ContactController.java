package rs.edu.raf.userservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domains.dto.contact.ContactCreateDto;
import rs.edu.raf.userservice.domains.dto.contact.ContactDto;
import rs.edu.raf.userservice.domains.dto.contact.ContactUpdateDto;
import rs.edu.raf.userservice.domains.dto.credit.CreateCreditDto;
import rs.edu.raf.userservice.domains.dto.credit.CreditDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeDto;
import rs.edu.raf.userservice.domains.dto.employee.EmployeeUpdateDto;
import rs.edu.raf.userservice.domains.dto.foreignaccount.ForeignAccountDto;
import rs.edu.raf.userservice.domains.model.Contact;
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ContactDto updateContact(@RequestBody ContactUpdateDto contactUpdateDto, @PathVariable Long id) {
        return contactService.updateContact(contactUpdateDto, id);
    }

    @DeleteMapping("/{contactId}")
    public void deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
    }
}
