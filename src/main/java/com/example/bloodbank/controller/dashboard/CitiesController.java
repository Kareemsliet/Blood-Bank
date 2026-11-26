package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.CityDto;
import com.example.bloodbank.dto.request.CityRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.CitiesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/cities")
@AllArgsConstructor
public class CitiesController {

    private final CitiesService citiesService;

    @GetMapping
    @PreAuthorize("hasAuthority('CITIES_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = citiesService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CITIES_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        CityDto city = this.citiesService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        city
                )
        );
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('CITIES_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody CityRequest data){
        this.citiesService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(
                        true,
                        "create new city successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('CITIES_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id,@RequestBody CityRequest data){
        this.citiesService.update(citiesService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "updated city successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CITIES_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id){
        boolean deleteStatus = this.citiesService.delete(citiesService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(
                        deleteStatus,
                        deleteStatus ? "delete city successfully" : "cant delete city,see clients or donations"
                )
        );
    }

}
