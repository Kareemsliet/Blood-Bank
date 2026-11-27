package com.example.bloodbank.service;

import com.example.bloodbank.dto.UserDto;
import com.example.bloodbank.dto.request.PasswordUpdateRequest;
import com.example.bloodbank.dto.request.UserRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.UserEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.RoleRepository;
import com.example.bloodbank.repository.UserRepository;
import jakarta.transaction.Transactional;
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
public class UsersService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserEntity findById(long id){
        return userRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("User with id {} was not found", id);
                    return new NotFoundIDException("User with id " + id + " not found");
                }
        );
    }

    public PaginationResponse getPagination(int pageNumber,String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<UserEntity> pagination = this.userRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex,pageSize, Sort.by("createdAt").descending())
        );
        List<UserDto> itemsPage = pagination.getContent().stream()
                .map(UserDto::toDto)
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

    public UserDto getById(long id){
        return UserDto.toDto(this.findById(id));
    }

    @Transactional
    public void create(@Valid UserRequest data){
        UserEntity userEntity = new UserEntity();
        applyRequest(userEntity, data);
        userEntity.setPassword(passwordEncoder.encode(data.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        UserEntity savedUser = this.userRepository.save(userEntity);
        log.info("Created user with id {}", savedUser.getId());
    }

    @Transactional
    public void update(UserEntity userEntity,@Valid UserRequest data){
        applyRequest(userEntity, data);
        userEntity.setUpdatedAt(LocalDateTime.now());
        this.userRepository.save(userEntity);
        log.info("Updated user with id {}", userEntity.getId());
    }

    private void applyRequest(UserEntity userEntity, UserRequest data) {
        userEntity.setName(data.getName());
        userEntity.setEmail(data.getEmail());
        userEntity.syncRoles(roleRepository.findAllById(data.getRoleIds()));
    }

    public void updatePassword(UserEntity userEntity,@Valid PasswordUpdateRequest data){
        userEntity.setPassword(passwordEncoder.encode(data.getPassword()));
        userEntity.setUpdatedAt(LocalDateTime.now());
        this.userRepository.save(userEntity);
        log.info("Updated password for user with id {}", userEntity.getId());
    }

    public void delete(UserEntity userEntity){
        this.userRepository.delete(userEntity);
        log.info("Deleted user with id {}", userEntity.getId());
    }

}
