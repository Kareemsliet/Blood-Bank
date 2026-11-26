package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.DonationDto;
import com.example.bloodbank.dto.request.DonationRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.DonationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/donations")
@AllArgsConstructor
public class DonationsController {

    private final DonationsService donationsService;

    @GetMapping
    @PreAuthorize("hasAuthority('DONATIONS_VIEW')")
    public ResponseEntity<MainResponse> index(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(name = "q", defaultValue = "") String query
    ) {
        PaginationResponse paginationResponse = donationsService.getPagination(pageNumber, query);
        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DONATIONS_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id) {
        DonationDto donation = donationsService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(true, null, donation)
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('DONATIONS_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody DonationRequest data) {
        donationsService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(true, "create new donation successfully")
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('DONATIONS_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id, @RequestBody DonationRequest data) {
        donationsService.update(donationsService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(true, "updated donation successfully")
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DONATIONS_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id) {
        donationsService.delete(donationsService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(true, "delete donation successfully")
        );
    }
}
