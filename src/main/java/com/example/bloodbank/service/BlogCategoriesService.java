package com.example.bloodbank.service;

import com.example.bloodbank.dto.BlogCategoryDto;
import com.example.bloodbank.dto.request.BlogCategoryRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.BlogCategoryEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.BlogCategoryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class BlogCategoriesService {

    private final BlogCategoryRepository categoriesRepository;

    public BlogCategoryEntity findById(long id){
        return categoriesRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Blog category with id {} was not found", id);
                    return new NotFoundIDException("Blog Category with id " + id + " not found");
                }
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<BlogCategoryEntity> pagination = this.categoriesRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex,pageSize, Sort.by("createdAt").descending())
        );
        List<BlogCategoryDto> itemsPage = pagination.getContent().stream()
                .map(BlogCategoryDto::toDto)
                .toList();
        return new PaginationResponse(
                itemsPage,
                pageNumber,
                pagination.getTotalPages(),
                pagination.hasNext(),
                pagination.hasPrevious(),
                pagination.getTotalElements()
        );
    }

    public BlogCategoryDto getById(long id){
        return BlogCategoryDto.toDto(this.findById(id));
    }

    public void create(@Valid BlogCategoryRequest data){
        BlogCategoryEntity categoryEntity = new BlogCategoryEntity();
        applyRequest(categoryEntity,data);
        categoryEntity.setCreatedAt(LocalDateTime.now());
        BlogCategoryEntity savedCategory = this.categoriesRepository.save(categoryEntity);
        log.info("Created blog category with id {}", savedCategory.getId());
    }

    public void update(BlogCategoryEntity categoryEntity,@Valid BlogCategoryRequest data){
        applyRequest(categoryEntity, data);
        categoryEntity.setUpdatedAt(LocalDateTime.now());
        this.categoriesRepository.save(categoryEntity);
        log.info("Updated blog category with id {}", categoryEntity.getId());
    }

    private void applyRequest(BlogCategoryEntity categoryEntity, BlogCategoryRequest data) {
        categoryEntity.setName(data.getName());
    }

    public boolean delete(BlogCategoryEntity categoryEntity){
        if(!categoryEntity.getBlogs().isEmpty()){
            log.warn("Cannot delete blog category with id {} because it still has blogs", categoryEntity.getId());
            return false;
        }
        this.categoriesRepository.delete(categoryEntity);
        log.info("Deleted blog category with id {}", categoryEntity.getId());
        return true;
    }
    
}
