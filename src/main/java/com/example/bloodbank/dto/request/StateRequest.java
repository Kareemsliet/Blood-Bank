package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.StateEntity;
import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateRequest {
    @NotBlank
    @UniqueColumn(column = "name",targetEntity = StateEntity.class)
    private String name;
}
