package com.example.bloodbank.service;

import com.example.bloodbank.dto.StateDto;
import com.example.bloodbank.dto.request.StateRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.StateEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.StateRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@AllArgsConstructor
public class StatesService {

    private final StateRepository stateRepository;

    public StateEntity findById(long id){
        return stateRepository.findById(id).orElseThrow(
                () -> new NotFoundIDException("State with id " + id + " not found")
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<StateEntity> pagination = this.stateRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex,pageSize, Sort.by("createdAt").descending())
        );
        List<StateDto> itemsPage = pagination.getContent().stream()
                .map(StateDto::toDto)
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

    public StateDto getById(long id){
        return StateDto.toDto(this.findById(id));
    }

    public void create(@Valid StateRequest data){
        StateEntity stateEntity = new StateEntity();
        applyRequest(stateEntity, data);
        stateEntity.setCreatedAt(LocalDateTime.now());
        this.stateRepository.save(stateEntity);
    }

    public void update(StateEntity stateEntity,@Valid StateRequest data){
        applyRequest(stateEntity, data);
        stateEntity.setUpdatedAt(LocalDateTime.now());
        this.stateRepository.save(stateEntity);
    }

    private void applyRequest(StateEntity stateEntity, StateRequest data) {
        stateEntity.setName(data.getName());
    }

    public boolean delete(StateEntity stateEntity){
        if(!stateEntity.getCities().isEmpty()){
            return false;
        }
        this.stateRepository.delete(stateEntity);
        return true;
    }
    
}
