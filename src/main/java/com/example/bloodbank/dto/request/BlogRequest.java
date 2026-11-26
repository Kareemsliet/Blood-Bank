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
    @NotBlank
    @UniqueColumn(column = "title",targetEntity = BlogEntity.class)
    private String title;
    @NotBlank
    @Size(max = 250)
    private String content;
    @Positive
    @ExistsColumn(targetEntity = BlogCategoryEntity.class)
    private long categoryId;
}
