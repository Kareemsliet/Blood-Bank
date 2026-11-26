package com.example.bloodbank.dto;

import com.example.bloodbank.entity.BlogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BlogDto {
    private long id;
    private String title;
    String content;
    private BlogCategoryDto category;
    private String urlImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static BlogDto toDto(BlogEntity blogEntity){
        return new BlogDto(
                blogEntity.getId(),
                blogEntity.getTitle(),
                blogEntity.getContent(),
                BlogCategoryDto.toDto(blogEntity.getCategory()),
                blogEntity.getImageUri(),
                blogEntity.getCreatedAt(),
                blogEntity.getUpdatedAt()
        );
    }
}
