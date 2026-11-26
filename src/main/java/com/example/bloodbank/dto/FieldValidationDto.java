package com.example.bloodbank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldValidationDto {
    private String field;
    private String message = null;
}
