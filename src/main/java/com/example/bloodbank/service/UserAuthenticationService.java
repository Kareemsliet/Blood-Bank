package com.example.bloodbank.service;

import com.example.bloodbank.dto.request.AuthenticationRequest;
import com.example.bloodbank.dto.response.AuthenticationResponse;
import com.example.bloodbank.entity.PersonalAccessTokenEntity;
import com.example.bloodbank.entity.UserEntity;
import com.example.bloodbank.exception.AuthenticationException;
import com.example.bloodbank.repository.PersonalAccessTokenRepository;
import com.example.bloodbank.repository.UserRepository;
import com.example.bloodbank.util.JwtToken;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
public class UserAuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonalAccessTokenRepository personalAccessTokenRepository;

    private final JwtToken jwtToken;

    private UserEntity attemptData(String email,String password) throws AuthenticationException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new AuthenticationException("email or password does not exist!");
        }
        String encodedPassword = user.get().getPassword();
        if(!passwordEncoder.matches(password,encodedPassword)){
            throw new AuthenticationException("email or password dose not exist!");
        }
        return user.get();
    }

    private PersonalAccessTokenEntity createNewPersonalAccessToken(long tokenableId){
        Date expirestAt = new Date(System.currentTimeMillis() + jwtToken.getJwtExpiration());
        PersonalAccessTokenEntity personalAccessToken = new PersonalAccessTokenEntity();
        personalAccessToken.setTokenableType("users");
        personalAccessToken.setTokenableId(tokenableId);
        personalAccessToken.setExpiresAt(expirestAt);
        personalAccessToken.setCreatedAt(new Date());
        return personalAccessTokenRepository.save(personalAccessToken);
    }

    @Transactional
    public AuthenticationResponse login(@Valid AuthenticationRequest data){
        UserEntity user = attemptData(data.getEmail(),data.getPassword());

        PersonalAccessTokenEntity personalAccessToken = createNewPersonalAccessToken(user.getId());
        String token = jwtToken.generateToken(
                personalAccessToken.getId(),
                "users",
                user.getId(),
                personalAccessToken.getExpiresAt()
        );

        return new AuthenticationResponse(
                token,
                personalAccessToken.getExpiresAt()
        );
    }

    @Transactional
    public void logout(String tokenId){
        personalAccessTokenRepository.deleteById(tokenId);
        SecurityContextHolder.clearContext();
    }

}
