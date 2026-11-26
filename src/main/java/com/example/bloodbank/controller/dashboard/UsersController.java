package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.UserDto;
import com.example.bloodbank.dto.request.PasswordUpdateRequest;
import com.example.bloodbank.dto.request.UserRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/users")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    @PreAuthorize("hasAuthority('USERS_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = usersService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USERS_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        UserDto user = this.usersService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        user
                )
        );
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('USERS_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody UserRequest data){
        this.usersService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(
                        true,
                        "create new user successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USERS_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id, @RequestBody UserRequest data){
        this.usersService.update(usersService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "updated user successfully"
                )
        );
    }

    @PostMapping("/{id}/change_password")
    @PreAuthorize("hasAuthority('USERS_EDIT')")
    public ResponseEntity<MainResponse> changePassword(@PathVariable long id, @RequestBody PasswordUpdateRequest data){
        this.usersService.updatePassword(usersService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "change password successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USERS_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id){
        this.usersService.delete(usersService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "delete user successfully"
                )
        );
    }

}
