package com.example.bloodbank.repository;

import com.example.bloodbank.entity.ContactEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity,Long>{
    Page<ContactEntity> findByEmailContaining(String email,Pageable pageable);
}
