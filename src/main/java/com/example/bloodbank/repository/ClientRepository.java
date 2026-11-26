package com.example.bloodbank.repository;

import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.entity.BlogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    Optional<ClientEntity> findByEmail(String email);
    Page<ClientEntity> findByNameContaining(String name,Pageable pageable);
    @Query("select blog from ClientEntity client join client.blogFavourites blog where client.id = :clientId")
    Page<BlogEntity> findBlogFavouritesByClientId(@Param("clientId") long clientId,Pageable pageable);
}
