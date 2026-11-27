package com.example.bloodbank.controller;

import com.example.bloodbank.dto.BlogDto;
import com.example.bloodbank.dto.DonationDto;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.AppService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AppController {

    private final AppService appService;

    @GetMapping("/blogs")
    public ResponseEntity<MainResponse> blogs(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(defaultValue = "", name = "q") String query
    ) {
        PaginationResponse blogs = appService.getBlogs(pageNumber, query);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<MainResponse> blog(@PathVariable long id) {
        BlogDto blog = appService.getBlog(id);
        return ResponseEntity.ok(new MainResponse(true, null, blog));
    }

    @GetMapping("/blog-categories")
    public ResponseEntity<MainResponse> blogCategories(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(defaultValue = "", name = "q") String query
    ) {
        PaginationResponse categories = appService.getBlogCategories(pageNumber, query);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/states")
    public ResponseEntity<MainResponse> states(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(defaultValue = "", name = "q") String query
    ) {
        PaginationResponse states = appService.getStates(pageNumber, query);
        return ResponseEntity.ok(states);
    }

    @GetMapping("/cities")
    public ResponseEntity<MainResponse> cities(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber,
            @RequestParam(defaultValue = "", name = "q") String query
    ) {
        PaginationResponse cities = appService.getCities(pageNumber, query);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/donations")
    public ResponseEntity<MainResponse> donations(
            @RequestParam(defaultValue = "1", name = "page") int pageNumber
    ) {
        PaginationResponse donations = appService.getDonations(pageNumber);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/donations/{id}")
    public ResponseEntity<MainResponse> donation(@PathVariable long id) {
        DonationDto donation = appService.getDonation(id);
        return ResponseEntity.ok(new MainResponse(true, null, donation));
    }

}
