package rs.edu.raf.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.userservice.domain.dto.contact.ContactDto;
import rs.edu.raf.userservice.domain.dto.contact.ContactPostPutDto;
import rs.edu.raf.userservice.service.ContactService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/contact")
public class ContactController {
    private final ContactService contactService;

    @Cacheable(value = "contactsByUser", key = "#userId")
    @GetMapping(path = "/{userId}")
    @Operation(description = "vraca sve kontakte od User-a")
    public List<ContactDto> getByUser(@PathVariable Long userId) {
        return contactService.findByUserId(userId);
    }


    @CacheEvict(value = "contactsByUser", key = "#userId")
    @PostMapping(path = "/{userId}")
    @Operation(description = "dodavanje novog kontakta za user-a, salje se UserId kom dodajemo")
    public ResponseEntity<?> createContact(@RequestBody ContactPostPutDto contactPostPutDto, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(contactService.createContact(contactPostPutDto, userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


    @CacheEvict(value = "contactsByUser", allEntries = true)
    @PutMapping(value = "/{contactId}")
    @Operation(description = "izmena kontakta za user-a, salje se contactId")
    public ResponseEntity<?> updateContact(@RequestBody ContactPostPutDto contactPostPutDto, @PathVariable Long contactId) {
        try {
            return ResponseEntity.ok(contactService.updateContact(contactPostPutDto, contactId));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


    @CacheEvict(value = "contactsByUser", allEntries = true)
    @DeleteMapping("/{contactId}")
    @Operation(description = "brisanje kontakta od user-a, salje se contactId")
    public ResponseEntity<?> deleteContact(@PathVariable Long contactId) {
        try {
            contactService.deleteContact(contactId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Coundn't delete contact with id: " + contactId);
        }
    }
}
