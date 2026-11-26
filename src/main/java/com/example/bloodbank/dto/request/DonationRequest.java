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

    @Positive
    @ExistsColumn(targetEntity = ClientEntity.class)
    private long clientId;

    @NotBlank
    private String name;

    @Positive
    private int age;

    @NotBlank
    private String phone;

    @Positive
    @ExistsColumn(targetEntity = CityEntity.class)
    private Long cityId;

    @NotBlank
    @EnumValue(targetEnum = BloodType.class,fieldName = "symbol")
    private String bloodType;

    private String comment;

    @NotBlank
    private String address;

    @Positive
    private int amount;

    @DecimalMin("-90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    private BigDecimal longitude;

    public BloodType getBloodType(){
        return BloodType.fromSymbol(this.bloodType);
    }
}
