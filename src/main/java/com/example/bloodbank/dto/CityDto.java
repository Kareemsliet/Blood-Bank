package com.example.bloodbank.dto;

import com.example.bloodbank.entity.CityEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CityDto {
    private long id;
    private String name;
    private StateDto state;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public static CityDto toDto(CityEntity cityEntity){
        return new CityDto(
                cityEntity.getId(),
                cityEntity.getName(),
                StateDto.toDto(cityEntity.getState()),
                cityEntity.getCreatedAt(),
                cityEntity.getUpdatedAt()
        );
    }
}
