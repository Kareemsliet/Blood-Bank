package com.example.bloodbank.entity;

import com.example.bloodbank.util.PersonalAccess;
import com.example.bloodbank.util.BloodType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@DynamicUpdate
public class ClientEntity implements PersonalAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "city_id",nullable = false)
    private long cityId;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BloodType bloodType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id",referencedColumnName = "id",insertable = false,updatable = false)
    private CityEntity city;
    @OneToMany(mappedBy = "client")
    private List<DonationEntity> donations = new ArrayList<>();
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "blog_client",
            joinColumns = @JoinColumn(name = "client_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id")
    )
    private Set<BlogEntity> blogFavourites = new HashSet<>();
    public void toggleBlog(BlogEntity blogEntity){
        if(getBlogFavourites().contains(blogEntity)){
            getBlogFavourites().remove(blogEntity);
        }else{
            getBlogFavourites().add(blogEntity);
        }
    }
}
