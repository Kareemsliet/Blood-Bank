package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.CityEntity;
import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.util.annotation.EnumValue;
import com.example.bloodbank.util.annotation.ExistsColumn;
import com.example.bloodbank.util.annotation.UniqueColumn;
import com.example.bloodbank.util.BloodType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @UniqueColumn(column = "email", targetEntity = ClientEntity.class,
            message = "An account with this email already exists")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Positive(message = "City ID must be a positive number")
    @ExistsColumn(targetEntity = CityEntity.class, message = "The selected city does not exist")
    private long cityId;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Blood type is required")
    @EnumValue(targetEnum = BloodType.class, fieldName = "symbol",
            message = "Blood type must be a valid blood type")
    private String bloodType;

    public BloodType getBloodType(){
        return BloodType.fromSymbol(this.bloodType);
    }

}
