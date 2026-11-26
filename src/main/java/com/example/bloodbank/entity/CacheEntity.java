package com.example.bloodbank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cache")
@Getter
@Setter
public class CacheEntity {
    @Id
    @Column(name = "`key`", nullable = false)
    private String key;
    @Column(name = "value", nullable = false)
    private String value;
    @Column(name = "expiration", nullable = false)
    private int expiration;
}
