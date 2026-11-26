package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.BlogCategoryDto;
import com.example.bloodbank.dto.request.BlogCategoryRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.BlogCategoriesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/blog_categories")
@AllArgsConstructor
public class BlogCategoriesController {

    private final BlogCategoriesService categoriesService;

    @GetMapping
    @PreAuthorize("hasAuthority('BLOG_CATEGORIES_VIEW')")
    public ResponseEntity<MainResponse> index(@RequestParam(defaultValue = "1",name = "page") int pageNumber, @RequestParam(name = "q",defaultValue = "") String query){
        PaginationResponse paginationResponse = categoriesService.getPagination(pageNumber,query);
        return ResponseEntity.status(HttpStatus.OK).body(
                paginationResponse
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BLOG_CATEGORIES_VIEW')")
    public ResponseEntity<MainResponse> show(@PathVariable long id){
        BlogCategoryDto category = this.categoriesService.getById(id);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        category
                )
        );
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('BLOG_CATEGORIES_CREATE')")
    public ResponseEntity<MainResponse> create(@RequestBody BlogCategoryRequest data){
        this.categoriesService.create(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MainResponse(
                        true,
                        "create new blog category successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('BLOG_CATEGORIES_EDIT')")
    public ResponseEntity<MainResponse> update(@PathVariable long id,@RequestBody BlogCategoryRequest data){
        this.categoriesService.update(categoriesService.findById(id), data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "updated blog category successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('BLOG_CATEGORIES_DELETE')")
    public ResponseEntity<MainResponse> delete(@PathVariable long id){
        boolean deleteStatus = this.categoriesService.delete(categoriesService.findById(id));
        return ResponseEntity.ok().body(
                new MainResponse(
                        deleteStatus,
                        deleteStatus ? "delete blog category successfully" : "category have some blogs,delete them first!"
                )
        );
    }

}
