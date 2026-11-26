package com.example.bloodbank.repository;

import com.example.bloodbank.entity.BlogCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategoryEntity,Long> {
    Page<BlogCategoryEntity> findByNameContaining(String name, Pageable pageable);
}
