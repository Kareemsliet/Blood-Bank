package com.example.bloodbank.entity;

import com.example.bloodbank.util.PersonalAccess;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyDiscriminatorValue;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Table(name = "personal_access_tokens")
@Getter
@Setter
public class PersonalAccessTokenEntity {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "tokenable_type",nullable = false)
    private String tokenableType;
    @Column(name = "tokenable_id",nullable = false)
    private long tokenableId;
    @Column(name = "expires_at",nullable = false)
    private Date expiresAt;
    @Column(name = "created_at")
    private Date createdAt;
    @Any(fetch = FetchType.LAZY)
    @AnyKeyJavaClass(Long.class)
    @JoinColumn(name="tokenable_id",updatable = false,insertable = false) //the foreign key column
    @Column(name="tokenable_type",insertable = false,updatable = false)   //the discriminator column
    @AnyDiscriminatorValue(discriminator = "users", entity = UserEntity.class)
    @AnyDiscriminatorValue(discriminator = "clients", entity = ClientEntity.class)
    private PersonalAccess tokenable;
}
