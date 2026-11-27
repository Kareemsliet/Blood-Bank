package com.example.bloodbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthPrincipalDto {
    private String tokenId;
    private String tokenableType;
    private long tokenableId;
    public static AuthPrincipalDto toDto(String tokenId,String tokenableType,long tokenableId){
        return new AuthPrincipalDto(
                tokenId,
                tokenableType,
                tokenableId
        );
    }
}
