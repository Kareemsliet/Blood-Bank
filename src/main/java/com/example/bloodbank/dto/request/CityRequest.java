package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.CityEntity;
import com.example.bloodbank.entity.StateEntity;
import com.example.bloodbank.util.annotation.ExistsColumn;
import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequest {
    @NotBlank
    @UniqueColumn(column = "name",targetEntity = CityEntity.class)
    private String name;
    @Positive(message = "the value not valid")
    @ExistsColumn(targetEntity = StateEntity.class)
    private long stateId;
}
