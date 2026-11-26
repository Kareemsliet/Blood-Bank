package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.ClientDto;
import com.example.bloodbank.dto.request.ClientRequest;
import com.example.bloodbank.dto.request.PasswordUpdateRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.ClientsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dashboard/clients")
@AllArgsConstructor
public class ClientsController {

    private final ClientsService clientsService;

    @GetMapping
    @PreAuthorize("hasAuthority('CLIENTS_VIEW')")
    public ResponseEntity<MainResponse> index(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(name = "q", defaultValue = "") String query
    ) {
        PaginationResponse paginationResponse = clientsService.getPagination(pageNumber, query);
        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTS_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id) {
        ClientDto client = clientsService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(true, null, client)
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CLIENTS_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody ClientRequest data) {
        clientsService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(true, "create new client successfully")
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTS_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id, @RequestBody ClientRequest data) {
        clientsService.update(clientsService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(true, "updated client successfully")
        );
    }

    @PostMapping("/{id}/change_password")
    @PreAuthorize("hasAuthority('CLIENTS_EDIT')")
    public ResponseEntity<MainResponse> changePassword(
            @PathVariable long id,
            @RequestBody PasswordUpdateRequest data
    ) {
        clientsService.updatePassword(clientsService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(true, "change password successfully")
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CLIENTS_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id) {
        clientsService.delete(clientsService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(true, "delete client successfully")
        );
    }
}
