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
    @NotBlank(message = "City name is required")
    @UniqueColumn(column = "name", targetEntity = CityEntity.class,
            message = "A city with this name already exists")
    private String name;
    @Positive(message = "State ID must be a positive number")
    @ExistsColumn(targetEntity = StateEntity.class, message = "The selected state does not exist")
    private long stateId;
}
