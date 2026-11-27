package com.example.bloodbank.entity;

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
@Table(name = "roles")
@Getter
@Setter
@DynamicUpdate
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "name", nullable = false,unique = true)
    private String name;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "role_has_permissions",
            joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id",referencedColumnName = "id")
    )
    @BatchSize(size = 14)
    private Set<PermissionEntity> permissions = new HashSet<>();
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();
    public void syncPermissions(List<PermissionEntity> permissions){
        this.permissions.retainAll(permissions);
        this.permissions.addAll(permissions);
    }
}
