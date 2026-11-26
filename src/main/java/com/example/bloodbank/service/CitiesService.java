package com.example.bloodbank.service;

import com.example.bloodbank.dto.CityDto;
import com.example.bloodbank.dto.request.CityRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.CityEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.CityRepository;
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
public class CitiesService {

    private final CityRepository cityRepository;

    public CityEntity findById(long id){
        return cityRepository.findById(id).orElseThrow(
                () -> new NotFoundIDException("City with id " + id + " not found")
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<CityEntity> pagination = this.cityRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex,pageSize, Sort.by("createdAt").descending())
        );
        List<CityDto> itemsPage = pagination.getContent().stream()
                .map(CityDto::toDto)
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

    public CityDto getById(long id){
        return CityDto.toDto(this.findById(id));
    }

    public void create(@Valid CityRequest data){
        CityEntity cityEntity = new CityEntity();
        applyRequest(cityEntity, data);
        cityEntity.setCreatedAt(LocalDateTime.now());
        this.cityRepository.save(cityEntity);
    }

    public void update(CityEntity cityEntity,@Valid CityRequest data){
        applyRequest(cityEntity, data);
        cityEntity.setUpdatedAt(LocalDateTime.now());
        this.cityRepository.save(cityEntity);
    }

    private void applyRequest(CityEntity cityEntity, CityRequest data) {
        cityEntity.setName(data.getName());
        cityEntity.setStateId(data.getStateId());
    }

    public boolean delete(CityEntity cityEntity){
        if(!cityEntity.getClients().isEmpty()){
            return false;
        }
        this.cityRepository.delete(cityEntity);
        return true;
    }

}
