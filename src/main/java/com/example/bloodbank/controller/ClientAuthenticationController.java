package com.example.bloodbank.controller;

import com.example.bloodbank.dto.AuthPrincipalDto;
import com.example.bloodbank.dto.request.AuthenticationRequest;
import com.example.bloodbank.dto.request.RegisterClientRequest;
import com.example.bloodbank.dto.response.AuthenticationResponse;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.service.ClientAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class ClientAuthenticationController {

    private final ClientAuthenticationService clientAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<MainResponse> register(@RequestBody RegisterClientRequest data) {
        AuthenticationResponse authenticationResponse =
                clientAuthenticationService.register(data);
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<MainResponse> login(@RequestBody AuthenticationRequest data){
        AuthenticationResponse auth = clientAuthenticationService.login(data);
        return ResponseEntity.ok().body(
                auth
        );
    }

    @PostMapping("/check")
    public ResponseEntity<MainResponse> check(@AuthenticationPrincipal AuthPrincipalDto principalDto){
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        principalDto != null && principalDto.getTokenableType().equals("clients")
                )
        );
    }

}
