package com.example.bloodbank.service;

import com.example.bloodbank.dto.ClientDto;
import com.example.bloodbank.dto.request.ClientRequest;
import com.example.bloodbank.dto.request.PasswordUpdateRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class ClientsService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientEntity findById(long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Client with id {} was not found", id);
                    return new NotFoundIDException("Client with id " + id + " not found");
                }
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query) {
        pageNumber = Math.max(pageNumber, 1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<ClientEntity> pagination = clientRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending())
        );
        List<ClientDto> itemsPage = pagination.getContent().stream()
                .map(ClientDto::toDto)
                .toList();
        return new PaginationResponse(
                itemsPage,
                pageNumber,
                pagination.getTotalPages(),
                pagination.hasNext(),
                pagination.hasPrevious(),
                pagination.getTotalElements()
        );
    }

    public ClientDto getById(long id) {
        return ClientDto.toDto(findById(id));
    }

    public void create(@Valid ClientRequest data) {
        ClientEntity clientEntity = new ClientEntity();
        applyRequest(clientEntity, data);
        clientEntity.setPassword(passwordEncoder.encode(data.getPassword()));
        clientEntity.setCreatedAt(LocalDateTime.now());
        ClientEntity savedClient = clientRepository.save(clientEntity);
        log.info("Created client with id {}", savedClient.getId());
    }

    public void update(ClientEntity clientEntity, @Valid ClientRequest data) {
        applyRequest(clientEntity, data);
        clientEntity.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(clientEntity);
        log.info("Updated client with id {}", clientEntity.getId());
    }

    private void applyRequest(ClientEntity clientEntity, ClientRequest data) {
        clientEntity.setName(data.getName());
        clientEntity.setEmail(data.getEmail());
        clientEntity.setCityId(data.getCityId());
        clientEntity.setBirthDate(data.getBirthDate());
        clientEntity.setBloodType(data.getBloodType());
    }

    public void updatePassword(ClientEntity clientEntity, @Valid PasswordUpdateRequest data) {
        clientEntity.setPassword(passwordEncoder.encode(data.getPassword()));
        clientEntity.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(clientEntity);
        log.info("Updated password for client with id {}", clientEntity.getId());
    }

    public void delete(ClientEntity clientEntity) {
        clientRepository.delete(clientEntity);
        log.info("Deleted client with id {}", clientEntity.getId());
    }

}
