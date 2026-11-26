package com.example.bloodbank.util;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.EnumeratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BloodType {

    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    @EnumeratedValue
    @JsonValue
    private final String symbol;

    public static BloodType fromSymbol(String symbol){
        BloodType bloodType = null;
        for (BloodType value : values()){
            if (value.getSymbol().equals(symbol)){
                bloodType = value;
                break;
            }
        }
        return bloodType;
    }

}
