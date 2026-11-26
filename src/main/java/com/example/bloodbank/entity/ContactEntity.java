package com.example.bloodbank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
