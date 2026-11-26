package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.BlogDto;
import com.example.bloodbank.dto.request.BlogRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.BlogsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/dashboard/blogs")
@AllArgsConstructor
public class BlogsController {

    private final BlogsService blogsService;

    @GetMapping
    @PreAuthorize("hasAuthority('BLOGS_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = blogsService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BLOGS_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        BlogDto blog = this.blogsService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        blog
                )
        );
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('BLOGS_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody BlogRequest data){
        this.blogsService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(
                        true,
                        "create new blog successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('BLOGS_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id,@RequestBody BlogRequest data){
        this.blogsService.update(blogsService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "updated blog successfully"
                )
        );
    }

    @PostMapping("/{id}/upload_image")
    @PreAuthorize("hasAuthority('BLOGS_EDIT')")
    public ResponseEntity<MainResponse> uploadImage(@PathVariable long id,@RequestParam(name = "image") MultipartFile image) throws IOException {
        this.blogsService.uploadImage(blogsService.findById(id),image);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "Successfully upload image"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('BLOGS_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id) throws IOException {
        this.blogsService.delete(blogsService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "delete blog successfully"
                )
        );
    }

}
