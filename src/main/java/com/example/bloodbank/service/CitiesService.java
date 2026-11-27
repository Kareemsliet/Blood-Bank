package com.example.bloodbank.service;

import com.example.bloodbank.dto.CityDto;
import com.example.bloodbank.dto.request.CityRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.CityEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.CityRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CitiesService {

    private final CityRepository cityRepository;

    public CityEntity findById(long id){
        return cityRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("City with id {} was not found", id);
                    return new NotFoundIDException("City with id " + id + " not found");
                }
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
        CityEntity savedCity = this.cityRepository.save(cityEntity);
        log.info("Created city with id {}", savedCity.getId());
    }

    public void update(CityEntity cityEntity,@Valid CityRequest data){
        applyRequest(cityEntity, data);
        cityEntity.setUpdatedAt(LocalDateTime.now());
        this.cityRepository.save(cityEntity);
        log.info("Updated city with id {}", cityEntity.getId());
    }

    private void applyRequest(CityEntity cityEntity, CityRequest data) {
        cityEntity.setName(data.getName());
        cityEntity.setStateId(data.getStateId());
    }

    public boolean delete(CityEntity cityEntity){
        if(!cityEntity.getClients().isEmpty()){
            log.warn("Cannot delete city with id {} because it still has clients", cityEntity.getId());
            return false;
        }
        this.cityRepository.delete(cityEntity);
        log.info("Deleted city with id {}", cityEntity.getId());
        return true;
    }

}
