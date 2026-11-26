package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.StateDto;
import com.example.bloodbank.dto.request.StateRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.StatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/states")
@AllArgsConstructor
public class StatesController {

    private final StatesService statesService;

    @GetMapping
    @PreAuthorize("hasAuthority('STATES_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = statesService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STATES_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        StateDto state = this.statesService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        state
                )
        );
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('STATES_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody StateRequest data){
        this.statesService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(
                        true,
                        "create new state successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('STATES_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id,@RequestBody StateRequest data){
        this.statesService.update(statesService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "updated state successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STATES_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id){
        boolean deleteStatus = this.statesService.delete(statesService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(
                        deleteStatus,
                        deleteStatus ? "delete state successfully" : "state have cities,delete them first!"
                )
        );
    }

}
