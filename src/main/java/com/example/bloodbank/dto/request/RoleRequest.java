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
    @NotBlank(message = "Role name is required")
    @UniqueColumn(column = "name", targetEntity = RoleEntity.class,
            message = "A role with this name already exists")
    private String name;
    @NotEmpty(message = "At least one permission is required")
    private List<
            @NotNull(message = "Permission ID cannot be null")
            @Positive(message = "Permission ID must be a positive number")
                    Long> permissionIds = new ArrayList<>();
}
