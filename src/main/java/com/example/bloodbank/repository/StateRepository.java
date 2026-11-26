package com.example.bloodbank.repository;

import com.example.bloodbank.entity.StateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<StateEntity,Long>{
    Page<StateEntity> findByNameContaining(String name, Pageable pageable);
}
