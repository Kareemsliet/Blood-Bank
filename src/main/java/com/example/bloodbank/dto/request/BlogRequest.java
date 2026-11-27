package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.BlogCategoryEntity;
import com.example.bloodbank.entity.BlogEntity;
import com.example.bloodbank.util.annotation.ExistsColumn;
import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogRequest {
    @NotBlank(message = "Blog title is required")
    @UniqueColumn(column = "title", targetEntity = BlogEntity.class,
            message = "A blog with this title already exists")
    private String title;
    @NotBlank(message = "Blog content is required")
    @Size(max = 250, message = "Blog content must be no more than 250 characters")
    private String content;
    @Positive(message = "Category ID must be a positive number")
    @ExistsColumn(targetEntity = BlogCategoryEntity.class,
            message = "The selected blog category does not exist")
    private long categoryId;
}
