package com.example.bloodbank.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtToken {

    @Getter
    private final long jwtExpiration;
    private final String jwtSecret;

    public JwtToken(@Value("${auth.jwt.secret}") String jwtSecret,@Value("${auth.jwt.expiration}") long jwtExpiration) {
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private JwtBuilder getJwtBuilder(
            String subject,
            String tokenable,
            long tokenableId,
            Date expiration
    ) {
        return Jwts.builder()
                .subject(subject)
                .claim("tokenable",tokenable)
                .claim("tokenableId",tokenableId)
                .expiration(expiration)
                .signWith(getSecretKey());
    }

    public String generateToken(String subject,String tokenable,long tokenableId,Date expiration) {
        return getJwtBuilder(subject,tokenable,tokenableId,expiration).compact();
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(getSecretKey()).build();
    }

    public Claims getTokenPayload(String token) {
        Claims tokenPayload = null;
        try {
           tokenPayload = getJwtParser().parseSignedClaims(token).getPayload();
       }catch (JwtException ex){
           System.out.println(ex.getMessage());
       }
        return tokenPayload;
    }

    public long getTokenableId(Claims payload){
        return payload.get("tokenableId",Long.class);
    }

    public String getTokenableType(Claims payload){
        return payload.get("tokenable",String.class);
    }

}

