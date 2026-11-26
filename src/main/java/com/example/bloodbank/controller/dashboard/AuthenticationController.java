package com.example.bloodbank.controller.dashboard;

import com.example.bloodbank.dto.AuthPrincipalDto;
import com.example.bloodbank.dto.request.AuthenticationRequest;
import com.example.bloodbank.dto.response.AuthenticationResponse;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboard/auth")
public class AuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<MainResponse> login(@RequestBody AuthenticationRequest data){
        AuthenticationResponse authenticationResponse = userAuthenticationService.login(data);
        return ResponseEntity.ok().body(
                authenticationResponse
        );
    }

    @PostMapping("/check")
    public ResponseEntity<MainResponse> check(@AuthenticationPrincipal AuthPrincipalDto principalDto){
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        principalDto != null && principalDto.getTokenableType().equals("users")
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<MainResponse> logout(@AuthenticationPrincipal AuthPrincipalDto principalDto){
        userAuthenticationService.logout(principalDto.getTokenId());
        return ResponseEntity.ok().body(new MainResponse(true,"logout successfully"));
    }

}
