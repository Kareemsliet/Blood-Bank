package com.example.bloodbank.entity;

import com.example.bloodbank.util.BloodType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
@Getter
@Setter
public class DonationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "client_id",nullable = false)
    private long clientId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "city_id",nullable = false)
    private long cityId;
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BloodType bloodType;
    @Column(name = "comment")
    private String comment;
    @Column(name = "address",nullable = false)
    private String address;
    @Column(name = "amount", nullable = false,comment = "number of blood bags")
    private int amount;
    @Column(name = "latitude")
    private BigDecimal latitude;
    @Column(name = "longitude")
    private BigDecimal longitude;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",referencedColumnName = "id",insertable = false,updatable = false)
    private ClientEntity client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id",referencedColumnName = "id",insertable = false,updatable = false)
    private CityEntity city;
}
