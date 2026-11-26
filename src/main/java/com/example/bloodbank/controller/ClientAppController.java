package com.example.bloodbank.controller;

import com.example.bloodbank.dto.AuthPrincipalDto;
import com.example.bloodbank.dto.DonationDto;
import com.example.bloodbank.dto.request.DonationRequest;
import com.example.bloodbank.dto.request.PasswordUpdateRequest;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.service.ClientAppService;
import com.example.bloodbank.service.ClientAuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/me")
public class ClientAppController {

    private final ClientAppService clientAppService;
    private final ClientAuthenticationService clientAuthenticationService;

    @GetMapping("/donations")
    public ResponseEntity<MainResponse> myDonations(
            @AuthenticationPrincipal AuthPrincipalDto principalDto,
            @RequestParam(defaultValue = "1", name = "page") int pageNumber
    ) {
        PaginationResponse donations = clientAppService.myDonations(
                principalDto.getTokenableId(),
                pageNumber
        );
        return ResponseEntity.ok().body(donations);
    }

    @GetMapping("/donations/{id}")
    public ResponseEntity<MainResponse> myDonation(
            @AuthenticationPrincipal AuthPrincipalDto principalDto,
            @PathVariable long id
    ) {
        System.out.println(principalDto.getTokenableType());
        DonationDto donation = clientAppService.myDonation(
                id,
                principalDto.getTokenableId()
        );
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        donation
                )
        );
    }

    @PostMapping("/donations/new")
    public ResponseEntity<MainResponse> newDonation(
            @AuthenticationPrincipal AuthPrincipalDto principalDto,
            @RequestBody DonationRequest data
    ) {
        data.setClientId(principalDto.getTokenableId());
        clientAppService.newDonation(data);
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "create new donation successfully"
                )
        );
    }

    @DeleteMapping("/donations/{id}")
    public ResponseEntity<MainResponse> deleteDonation(
            @AuthenticationPrincipal AuthPrincipalDto principalDto,
            @PathVariable long id
    ) {
        clientAppService.deleteDonation(clientAppService.findDonation(id,principalDto.getTokenableId()));
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        "delete donation successfully"
                )
        );
    }

    @GetMapping("/favourites/blogs")
    public ResponseEntity<MainResponse> favouriteBlogs(
            @AuthenticationPrincipal AuthPrincipalDto principalDto,
            @RequestParam(defaultValue = "1", name = "page") int pageNumber
    ) {
        PaginationResponse blogs = clientAppService.favouriteBlogs(
                principalDto.getTokenableId(),
                pageNumber
        );
        return ResponseEntity.ok().body(
                new MainResponse(
                        true,
                        null,
                        blogs
                )
        );
    }

    @PostMapping("/favourites/blogs/{id}/toggle")
    public ResponseEntity<MainResponse> toggleBlog(
            @AuthenticationPrincipal AuthPrincipalDto principalDto,
            @PathVariable long id
    ) {
        clientAppService.toggleBlog(principalDto.getTokenableId(),id);
        return ResponseEntity.ok().body(
                new MainResponse(true,
                        "toggle blog successfully"
                )
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<MainResponse> updatePassword(@AuthenticationPrincipal AuthPrincipalDto principalDto,@RequestBody PasswordUpdateRequest data){
        clientAuthenticationService.updatePassword(principalDto.getTokenableId(),data);
        return ResponseEntity.ok().body(
                new MainResponse(true, "change password successfully")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<MainResponse> logout(@AuthenticationPrincipal AuthPrincipalDto principalDto){
        clientAuthenticationService.logout(principalDto.getTokenId());
        return ResponseEntity.ok().body(new MainResponse(true, "logout successfully"));
    }

}
