package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.RoleDto;
import com.example.bloodbank.dto.request.RoleRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.RolesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/roles")
@AllArgsConstructor
public class RolesController {

    private final RolesService rolesService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLES_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber,@RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = rolesService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLES_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        RoleDto role = this.rolesService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        role
                )
        );
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('ROLES_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody RoleRequest data){
        this.rolesService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(
                        true,
                        "create new role successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLES_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id,@RequestBody RoleRequest data){
        this.rolesService.update(rolesService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "updated role successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLES_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id){
        this.rolesService.delete(rolesService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "delete role successfully"
                )
        );
    }
    
}
