package com.example.bloodbank.dto;

import com.example.bloodbank.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String email;
    private List<RoleDto> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static UserDto toDto(UserEntity userEntity){
        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getRoles()
                        .stream()
                        .map(roleEntity -> new RoleDto(
                                roleEntity.getId(),
                                roleEntity.getName()
                        ))
                        .toList(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }
}
