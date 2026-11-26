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
    @NotBlank
    private String name;

    @NotBlank
    @Email
    @UniqueColumn(column = "email", targetEntity = ClientEntity.class)
    private String email;

    @Size(min = 8)
    private String password;

    @Positive(message = "the value not valid")
    @ExistsColumn(targetEntity = CityEntity.class)
    private long cityId;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotBlank
    @EnumValue(targetEnum = BloodType.class,fieldName = "symbol")
    private String bloodType;

    public BloodType getBloodType(){
        return BloodType.fromSymbol(this.bloodType);
    }

}
