package com.example.bloodbank.dto;

import com.example.bloodbank.entity.PermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PermissionDto {
    private long id;
    private String name;
    public static PermissionDto toDto(PermissionEntity permissionEntity){
        return new PermissionDto(
                permissionEntity.getId(),
                permissionEntity.getName()
        );
    }
}
