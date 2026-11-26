package com.example.bloodbank.entity;

import com.example.bloodbank.util.PersonalAccess;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@DynamicUpdate
public class UserEntity implements PersonalAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    @Column(name = "email",nullable = false, unique = true)
    private String email;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "user_has_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )
    @BatchSize(size = 20)
    private Set<RoleEntity> roles = new HashSet<>();
    public void syncRoles(List<RoleEntity> roles){
        this.roles.retainAll(roles);
        this.roles.addAll(roles);
    }
}
