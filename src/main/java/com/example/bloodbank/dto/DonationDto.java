package com.example.bloodbank.dto;

import com.example.bloodbank.entity.DonationEntity;
import com.example.bloodbank.util.BloodType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DonationDto {
    private long id;
    private long clientId;
    private String name;
    private int age;
    private String phone;
    private CityDto city;
    private BloodType bloodType;
    private String comment;
    private String address;
    private int amount;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DonationDto toDto(DonationEntity donationEntity) {
        return new DonationDto(
                donationEntity.getId(),
                donationEntity.getClientId(),
                donationEntity.getName(),
                donationEntity.getAge(),
                donationEntity.getPhone(),
                CityDto.toDto(donationEntity.getCity()),
                donationEntity.getBloodType(),
                donationEntity.getComment(),
                donationEntity.getAddress(),
                donationEntity.getAmount(),
                donationEntity.getLatitude(),
                donationEntity.getLongitude(),
                donationEntity.getCreatedAt(),
                donationEntity.getUpdatedAt()
        );
    }
}
