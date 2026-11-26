package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.RoleEntity;
import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleRequest {
    @NotBlank
    @UniqueColumn(column = "name",targetEntity = RoleEntity.class)
    private String name;
    @NotEmpty
    private List<@NotNull @Positive Long> permissionIds = new ArrayList<>();
}
