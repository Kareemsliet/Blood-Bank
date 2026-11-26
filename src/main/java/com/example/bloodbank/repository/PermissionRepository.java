package com.example.bloodbank.repository;

import com.example.bloodbank.entity.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionRepository extends JpaRepository<PermissionEntity,Long>{
    Page<PermissionEntity> findByNameContaining(String name,Pageable pageable);
    @Query("""
            select distinct permission.authority
            from UserEntity user
            join user.roles role
            join role.permissions permission
            where user.id = :userId
            """)
    Set<String> findAuthoritiesByUserId(@Param("userId") long userId);
}
