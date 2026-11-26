package com.example.bloodbank.dto;

import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.util.BloodType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ClientDto {
    private long id;
    private String name;
    private String email;
    private CityDto city;
    private LocalDate birthDate;
    private BloodType bloodType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public ClientDto(long id,String name,BloodType bloodType){
        this.id = id;
        this.name = name;
        this.bloodType = bloodType;
    }
    public static ClientDto toDto(ClientEntity clientEntity) {
        return new ClientDto(
                clientEntity.getId(),
                clientEntity.getName(),
                clientEntity.getEmail(),
                CityDto.toDto(clientEntity.getCity()),
                clientEntity.getBirthDate(),
                clientEntity.getBloodType(),
                clientEntity.getCreatedAt(),
                clientEntity.getUpdatedAt()
        );
    }
}
