package com.example.bloodbank.dto;

import com.example.bloodbank.entity.StateEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StateDto {
    private long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static StateDto toDto(StateEntity stateEntity){
        return new StateDto(
                stateEntity.getId(),
                stateEntity.getName(),
                stateEntity.getCreatedAt(),
                stateEntity.getUpdatedAt()
        );
    }
}
