package com.example.bloodbank.dto;

import com.example.bloodbank.entity.BlogCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BlogCategoryDto {
    private long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static BlogCategoryDto toDto(BlogCategoryEntity categoryEntity){
        return new BlogCategoryDto(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getUpdatedAt()
        );
    }
}
