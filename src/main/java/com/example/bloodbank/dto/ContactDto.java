package com.example.bloodbank.dto;

import com.example.bloodbank.entity.ContactEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ContactDto {
    private long id;
    private String title;
    private String phone;
    private String email;
    private String comment;
    private LocalDateTime createdAt;
    public static ContactDto toDto(ContactEntity contactEntity){
        return new ContactDto(
                contactEntity.getId(),
                contactEntity.getTitle(),
                contactEntity.getPhone(),
                contactEntity.getEmail(),
                contactEntity.getComment(),
                contactEntity.getCreatedAt()
        );
    }
}
