package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.UserEntity;
import com.example.bloodbank.util.annotation.UniqueColumn;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequest {
    @NotEmpty
    private String name;
    @NotBlank
    @Email
    @UniqueColumn(column = "email",targetEntity = UserEntity.class)
    private String email;
    @Size(min = 8)
    @NotBlank
    private String password;
    @NotEmpty
    private List<@NotNull @Positive Long> roleIds;
}
