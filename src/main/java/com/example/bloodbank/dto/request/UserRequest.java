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
    @NotEmpty(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @UniqueColumn(column = "email", targetEntity = UserEntity.class,
            message = "An account with this email already exists")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotEmpty(message = "At least one role is required")
    private List<
            @NotNull(message = "Role ID cannot be null")
            @Positive(message = "Role ID must be a positive number")
                    Long> roleIds;
}
