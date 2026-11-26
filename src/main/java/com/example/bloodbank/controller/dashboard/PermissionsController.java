package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.PermissionDto;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.PermissionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dashboard/permissions")
@AllArgsConstructor
public class PermissionsController {

    private final PermissionsService permissionsService;

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSIONS_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = permissionsService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSIONS_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        PermissionDto permission = this.permissionsService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        permission
                )
        );
    }

}
