package com.example.bloodbank.service;

import com.example.bloodbank.dto.BlogCategoryDto;
import com.example.bloodbank.dto.request.BlogCategoryRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.BlogCategoryEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.BlogCategoryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
public class BlogCategoriesService {

    private final BlogCategoryRepository categoriesRepository;

    public BlogCategoryEntity findById(long id){
        return categoriesRepository.findById(id).orElseThrow(
                () -> new NotFoundIDException("Blog Category with id " + id + " not found")
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
        this.categoriesRepository.save(categoryEntity);
    }

    public void update(BlogCategoryEntity categoryEntity,@Valid BlogCategoryRequest data){
        applyRequest(categoryEntity, data);
        categoryEntity.setUpdatedAt(LocalDateTime.now());
        this.categoriesRepository.save(categoryEntity);
    }

    private void applyRequest(BlogCategoryEntity categoryEntity, BlogCategoryRequest data) {
        categoryEntity.setName(data.getName());
    }

    public boolean delete(BlogCategoryEntity categoryEntity){
        if(!categoryEntity.getBlogs().isEmpty()){
            return false;
        }
        this.categoriesRepository.delete(categoryEntity);
        return true;
    }
    
}
