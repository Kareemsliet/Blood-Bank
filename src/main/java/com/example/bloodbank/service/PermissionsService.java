package com.example.bloodbank.service;

import com.example.bloodbank.dto.PermissionDto;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.PermissionEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PermissionsService {

    private final PermissionRepository permissionRepository;

    public PermissionEntity findById(long id){
        return permissionRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Permission with id {} was not found", id);
                    return new NotFoundIDException("Permission with id " + id + " not found");
                }
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<PermissionEntity> pagination = this.permissionRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex,pageSize)
        );
        List<PermissionDto> itemsPage = pagination.getContent().stream()
                .map(PermissionDto::toDto)
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

    public PermissionDto getById(long id){
        return PermissionDto.toDto(this.findById(id));
    }

}
