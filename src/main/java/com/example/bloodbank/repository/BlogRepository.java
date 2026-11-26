package com.example.bloodbank.repository;

import com.example.bloodbank.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity,Long> {
    Page<BlogEntity> findByTitleContaining(String title,Pageable pageable);
}
