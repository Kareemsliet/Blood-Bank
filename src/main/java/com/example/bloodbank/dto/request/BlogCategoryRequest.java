package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.BlogCategoryEntity;
import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogCategoryRequest {
    @NotBlank
    @UniqueColumn(column = "name",targetEntity = BlogCategoryEntity.class)
    private String name;
}
