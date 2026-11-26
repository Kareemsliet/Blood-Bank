package com.example.bloodbank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@DynamicUpdate
public class BlogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "title", nullable = false,unique = true)
    private String title;
    @Column(name = "image")
    private String image = null;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "cat_id",nullable = false)
    private long categoryId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id",referencedColumnName = "id",insertable = false,updatable = false)
    private BlogCategoryEntity category;

    public String getImageUri(){
        String uri = null;
        if(getImage() != null){
            uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("uploads/blogs/" + image)
                    .toUriString();
        }
        return uri;
    }

}
