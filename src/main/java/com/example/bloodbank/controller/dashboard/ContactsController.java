package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.ContactDto;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.ContactsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dashboard/contacts")
@AllArgsConstructor
public class ContactsController {

    private final ContactsService contactsService;

    @GetMapping
    @PreAuthorize("hasAuthority('CONTACTS_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = contactsService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CONTACTS_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        ContactDto contact = this.contactsService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        contact
                )
        );
    }

}
