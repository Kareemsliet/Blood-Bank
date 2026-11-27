package com.example.bloodbank.dto.request;

import com.example.bloodbank.entity.CityEntity;
import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.util.annotation.EnumValue;
import com.example.bloodbank.util.annotation.ExistsColumn;
import com.example.bloodbank.util.BloodType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DonationRequest {

    @Positive(message = "Client ID must be a positive number")
    @ExistsColumn(targetEntity = ClientEntity.class, message = "The selected client does not exist")
    private long clientId;

    @NotBlank(message = "Name is required")
    private String name;

    @Positive(message = "Age must be a positive number")
    private int age;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Positive(message = "City ID must be a positive number")
    @ExistsColumn(targetEntity = CityEntity.class, message = "The selected city does not exist")
    private Long cityId;

    @NotBlank(message = "Blood type is required")
    @EnumValue(targetEnum = BloodType.class, fieldName = "symbol",
            message = "Blood type must be a valid blood type")
    private String bloodType;

    private String comment;

    @NotBlank(message = "Address is required")
    private String address;

    @Positive(message = "Donation amount must be a positive number")
    private int amount;

    @DecimalMin(value = "-90.0", message = "Latitude must be at least -90.0")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be at least -180.0")
    private BigDecimal longitude;

    public BloodType getBloodType(){
        return BloodType.fromSymbol(this.bloodType);
    }

}
