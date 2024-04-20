package rs.edu.raf.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.contact.ContactCreateDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactUpdateDto;
import rs.edu.raf.userservice.service.ContactService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/contact")
public class
ContactController {

    private ContactService contactService;

    @GetMapping("/getAll")
    public List<ContactDto> getAll() {
        return contactService.findAll();
    }

    @GetMapping(path = "/{userId}")
    public List<ContactDto> getByUser(@PathVariable Long userId) {
        return contactService.findByUserId(userId);
    }

    @PostMapping(path = "/{userId}")
    public ContactDto createContact(@RequestBody ContactCreateDto contactCreateDto, @PathVariable Long userId) {
        return contactService.createContact(contactCreateDto, userId);
    }

    @PutMapping(value = "/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ContactDto updateContact(@RequestBody ContactUpdateDto contactUpdateDto, @PathVariable Long contactId) {
        return contactService.updateContact(contactUpdateDto, contactId);
    }

    @DeleteMapping("/{contactId}")
    public void deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
    }
}
