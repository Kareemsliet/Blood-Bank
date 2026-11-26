package com.example.bloodbank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthPrincipalDto {
    private String tokenId;
    private String tokenableType;
    private long tokenableId;
    private String name = null;
    private String email = null;
    public AuthPrincipalDto(String tokenId,String tokenableType,long tokenableId){
        this.tokenId = tokenId;
        this.tokenableType = tokenableType;
        this.tokenableId = tokenableId;
    }
    public static AuthPrincipalDto toDto(String tokenId,String tokenableType,long tokenableId){
        return new AuthPrincipalDto(
                tokenId,
                tokenableType,
                tokenableId
        );
    }
}
