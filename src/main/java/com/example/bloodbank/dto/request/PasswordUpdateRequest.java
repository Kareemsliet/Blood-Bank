package com.example.bloodbank.dto.request;

import com.example.bloodbank.util.annotation.ConfirmedPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfirmedPassword
public class PasswordUpdateRequest {
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotBlank
    @Size(min = 8)
    private String confirmedPassword;
}
