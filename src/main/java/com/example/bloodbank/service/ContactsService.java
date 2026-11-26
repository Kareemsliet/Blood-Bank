package com.example.bloodbank.service;

import com.example.bloodbank.dto.ContactDto;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.ContactEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactsService {

    private final ContactRepository contactsRepository;

    public ContactEntity findById(long id){
        return contactsRepository.findById(id).orElseThrow(
                () -> new NotFoundIDException("Contact with id " + id + " not found")
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<ContactEntity> pagination = this.contactsRepository.findByEmailContaining(
                query,
                PageRequest.of(pageIndex,pageSize)
        );
        List<ContactDto> itemsPage = pagination.getContent().stream()
                .map(ContactDto::toDto)
                .toList();
        return new PaginationResponse(
                itemsPage,
                pageNumber,
                pagination.getTotalPages(),
                pagination.hasNext(),
                pagination.hasPrevious(),
                pagination.getTotalElements()
        );
    }

    public ContactDto getById(long id){
        return ContactDto.toDto(this.findById(id));
    }

}
