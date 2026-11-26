package com.example.bloodbank.repository;

import com.example.bloodbank.entity.PersonalAccessTokenEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalAccessTokenRepository extends JpaRepository<PersonalAccessTokenEntity,String> {
    @Modifying
    @Query("""
    delete from PersonalAccessTokenEntity p where p.id = :id
    """)
    void deleteById(@Param("id") @NonNull String id);
}
