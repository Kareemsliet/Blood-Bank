package com.example.bloodbank.dto.request;

import com.example.bloodbank.util.annotation.ConfirmedPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfirmedPassword(message = "Password and confirmation do not match")
public class RegisterClientRequest extends ClientRequest {
    @NotBlank(message = "Password confirmation is required")
    @Size(min = 8, message = "Password confirmation must be at least 8 characters long")
    private String confirmedPassword;
}
