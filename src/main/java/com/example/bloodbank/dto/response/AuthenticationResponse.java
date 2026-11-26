package com.example.bloodbank.dto.response;

import java.util.Date;

public class AuthenticationResponse extends MainResponse {
    public AuthenticationResponse(String token, Date expiresAt){
        super(
                true,
                null,
                new data(
                        token,
                        expiresAt
                )
        );
    }
    private record data(String token,Date expiresAt){}
}
