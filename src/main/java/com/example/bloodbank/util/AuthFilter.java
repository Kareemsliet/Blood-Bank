package com.example.bloodbank.util;

import com.example.bloodbank.dto.AuthPrincipalDto;
import com.example.bloodbank.entity.PersonalAccessTokenEntity;
import com.example.bloodbank.repository.PersonalAccessTokenRepository;
import com.example.bloodbank.repository.PermissionRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter{

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";

    private final PermissionRepository permissionRepository;
    private final PersonalAccessTokenRepository personalAccessTokenRepository;
    private final JwtToken jwtToken;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HEADER_NAME);
        if (authHeader == null || !authHeader.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            filterChain.doFilter(request,response);
            return;
        }

        Claims tokenPayload = jwtToken.getTokenPayload(token);
        if (tokenPayload == null) {
            filterChain.doFilter(request,response);
            return;
        }

        long tokenableId = jwtToken.getTokenableId(tokenPayload);
        String tokenableType = jwtToken.getTokenableType(tokenPayload);
        String tokenId = tokenPayload.getSubject();
        personalAccessTokenRepository.findById(tokenId)
                .filter(savedToken -> savedToken.getTokenableId() == tokenableId)
                .filter(savedToken -> savedToken.getTokenableType().equals(tokenableType))
                .filter(savedToken -> savedToken.getExpiresAt().after(new Date()))
                .filter(savedToken -> isTokenAllowedForRequest(tokenableType,request))
                .ifPresent(savedToken -> setAuthentication(savedToken,request));

        filterChain.doFilter(request,response);
    }

    private boolean isTokenAllowedForRequest(String tokenableType, HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.startsWith("/api/dashboard/") && tokenableType.equals("users")) {
            return true;
        }
        return (path.startsWith("/api/me/") || path.equals("/api/auth/check")) && tokenableType.equals("clients");
    }

    private void setAuthentication(
            PersonalAccessTokenEntity token,
            HttpServletRequest request
    ) {
        AuthPrincipalDto authPrincipalDto = AuthPrincipalDto.toDto(token.getId(),token.getTokenableType(),token.getTokenableId());

        List<SimpleGrantedAuthority> authorities = getAuthorities(token.getTokenableId(),token.getTokenableType());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authPrincipalDto,null,authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private List<SimpleGrantedAuthority> getAuthorities(long tokenableId,String tokenableType){
        List<SimpleGrantedAuthority> authorities = List.of();
        if(tokenableType.equals("users")){
            authorities = permissionRepository.findAuthoritiesByUserId(tokenableId)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
        return authorities;
    }

}
