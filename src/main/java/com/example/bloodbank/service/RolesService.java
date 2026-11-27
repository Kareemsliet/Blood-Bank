package com.example.bloodbank.service;

import com.example.bloodbank.dto.RoleDto;
import com.example.bloodbank.dto.request.RoleRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.RoleEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.PermissionRepository;
import com.example.bloodbank.repository.RoleRepository;
import jakarta.transaction.Transactional;
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
public class RolesService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleEntity findById(long id){
        return roleRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Role with id {} was not found", id);
                    return new NotFoundIDException("Role with id " + id + " not found");
                }
        );
    }

    public PaginationResponse getPagination(int pageNumber,String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<RoleEntity> pagination = this.roleRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex,pageSize, Sort.by("createdAt").descending())
        );
        List<RoleDto> itemsPage = pagination.getContent().stream()
                .map(RoleDto::toDto)
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

    public RoleDto getById(long id){
        return RoleDto.toDto(this.findById(id));
    }

    @Transactional
    public void create(@Valid RoleRequest data){
        RoleEntity roleEntity = new RoleEntity();
        applyRequest(roleEntity, data);
        roleEntity.setCreatedAt(LocalDateTime.now());
        RoleEntity savedRole = this.roleRepository.save(roleEntity);
        log.info("Created role with id {}", savedRole.getId());
    }

    @Transactional
    public void update(RoleEntity roleEntity,@Valid RoleRequest data){
        applyRequest(roleEntity, data);
        roleEntity.setUpdatedAt(LocalDateTime.now());
        this.roleRepository.save(roleEntity);
        log.info("Updated role with id {}", roleEntity.getId());
    }

    private void applyRequest(RoleEntity roleEntity, RoleRequest data) {
        roleEntity.setName(data.getName());
        roleEntity.syncPermissions(permissionRepository.findAllById(data.getPermissionIds()));
    }

    public void delete(RoleEntity roleEntity){
        this.roleRepository.delete(roleEntity);
        log.info("Deleted role with id {}", roleEntity.getId());
    }
    
}
