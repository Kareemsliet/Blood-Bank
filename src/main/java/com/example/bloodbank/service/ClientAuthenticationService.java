package com.example.bloodbank.service;

import com.example.bloodbank.dto.request.AuthenticationRequest;
import com.example.bloodbank.dto.request.PasswordUpdateRequest;
import com.example.bloodbank.dto.request.RegisterClientRequest;
import com.example.bloodbank.dto.response.AuthenticationResponse;
import com.example.bloodbank.entity.PersonalAccessTokenEntity;
import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.exception.AuthenticationException;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.ClientRepository;
import com.example.bloodbank.repository.PersonalAccessTokenRepository;
import com.example.bloodbank.util.JwtToken;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class ClientAuthenticationService {
    
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonalAccessTokenRepository personalAccessTokenRepository;
    
    private final JwtToken jwtToken;

    private ClientEntity attempt(String email, String password) throws AuthenticationException {
        Optional<ClientEntity> client = clientRepository.findByEmail(email);
        if(client.isEmpty()){
            log.warn("Client authentication failed");
            throw new AuthenticationException("email or password not found");
        }
        String encodedPassword = client.get().getPassword();
        if(!passwordEncoder.matches(password,encodedPassword)){
            log.warn("Client authentication failed");
            throw new AuthenticationException("email or password no found");
        }
        return client.get();
    }

    private PersonalAccessTokenEntity createNewPersonalAccessToken(long tokenableId){
        Date expiresAt = new Date(System.currentTimeMillis() + jwtToken.getJwtExpiration());
        PersonalAccessTokenEntity personalAccessToken = new PersonalAccessTokenEntity();
        personalAccessToken.setTokenableType("clients");
        personalAccessToken.setTokenableId(tokenableId);
        personalAccessToken.setCreatedAt(new Date());
        personalAccessToken.setExpiresAt(expiresAt);
        return personalAccessTokenRepository.save(personalAccessToken);
    }

    @Transactional
    public AuthenticationResponse register(@Valid RegisterClientRequest data) {
        ClientEntity client = applyRequest(data);

        PersonalAccessTokenEntity personalAccessToken = createNewPersonalAccessToken(client.getId());
        log.info("Registered client with id {}", client.getId());
        String token = jwtToken.generateToken(
                personalAccessToken.getId(),
                "clients",
                client.getId(),
                personalAccessToken.getExpiresAt()
        );

        return new AuthenticationResponse(
                token,
                personalAccessToken.getExpiresAt()
        );
    }

    private ClientEntity applyRequest(RegisterClientRequest data) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(data.getName());
        clientEntity.setEmail(data.getEmail());
        clientEntity.setPassword(passwordEncoder.encode(data.getConfirmedPassword()));
        clientEntity.setCityId(data.getCityId());
        clientEntity.setBirthDate(data.getBirthDate());
        clientEntity.setBloodType(data.getBloodType());
        clientEntity.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(clientEntity);
    }

    @Transactional
    public AuthenticationResponse login(@Valid AuthenticationRequest data){
        ClientEntity client = attempt(data.getEmail(),data.getPassword());

        PersonalAccessTokenEntity personalAccessToken = createNewPersonalAccessToken(client.getId());
        log.info("Client with id {} authenticated successfully", client.getId());
        String token = jwtToken.generateToken(
                personalAccessToken.getId(),
                "clients",
                client.getId(),
                personalAccessToken.getExpiresAt()
        );

        return new AuthenticationResponse(
                token,
                personalAccessToken.getExpiresAt()
        );
    }

    @Transactional
    public void updatePassword(long clientId,@Valid PasswordUpdateRequest data){
        ClientEntity client = clientRepository.findById(clientId).orElseThrow(
                () -> new NotFoundIDException("Client with id " + clientId + " not found")
        );
        client.setPassword(passwordEncoder.encode(data.getPassword()));
        client.setUpdatedAt(LocalDateTime.now());
        this.clientRepository.save(client);
        log.info("Updated password for client with id {}", clientId);
    }

    @Transactional
    public void logout(String tokenId){
        personalAccessTokenRepository.deleteById(tokenId);
        SecurityContextHolder.clearContext();
        log.info("Client logged out successfully");
    }

}
