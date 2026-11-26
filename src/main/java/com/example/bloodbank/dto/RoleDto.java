package com.example.bloodbank.dto;

import com.example.bloodbank.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoleDto {
    private long id;
    private String name;
    private List<PermissionDto> permissions = new ArrayList<>();
    private LocalDateTime createdAt = null;
    private LocalDateTime updatedAt = null;
    public RoleDto(long id,String name){
        this.id = id;
        this.name = name;
    }
    public static RoleDto toDto(RoleEntity roleEntity){
        return new RoleDto(
                roleEntity.getId(),
                roleEntity.getName(),
                roleEntity.getPermissions().stream().map(PermissionDto::toDto).toList(),
                roleEntity.getCreatedAt(),
                roleEntity.getUpdatedAt()
        );
    }
}
