package com.example.bloodbank.repository;

import com.example.bloodbank.entity.DonationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<DonationEntity, Long> {
    Page<DonationEntity> findByNameContaining(String name, Pageable pageable);
    Page<DonationEntity> findByClientId(long clientId,Pageable pageable);
    Optional<DonationEntity> findByIdAndClientId(long id, long clientId);
}
