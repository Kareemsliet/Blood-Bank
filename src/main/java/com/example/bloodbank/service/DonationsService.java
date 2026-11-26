package com.example.bloodbank.service;

import com.example.bloodbank.dto.DonationDto;
import com.example.bloodbank.dto.request.DonationRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.DonationEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.DonationRepository;
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
public class DonationsService {

    private final DonationRepository donationRepository;

    public DonationEntity findById(long id) {
        return donationRepository.findById(id).orElseThrow(
                () -> new NotFoundIDException("Donation with id " + id + " not found")
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query) {
        pageNumber = Math.max(pageNumber, 1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<DonationEntity> pagination = donationRepository.findByNameContaining(
                query,
                PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending())
        );
        List<DonationDto> itemsPage = pagination.getContent().stream()
                .map(DonationDto::toDto)
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

    public DonationDto getById(long id) {
        return DonationDto.toDto(findById(id));
    }

    public void create(@Valid DonationRequest data) {
        DonationEntity donationEntity = new DonationEntity();
        applyRequest(donationEntity, data);
        donationEntity.setCreatedAt(LocalDateTime.now());
        donationRepository.save(donationEntity);
    }

    public void update(DonationEntity donationEntity, @Valid DonationRequest data) {
        applyRequest(donationEntity, data);
        donationEntity.setUpdatedAt(LocalDateTime.now());
        donationRepository.save(donationEntity);
    }

    public void delete(DonationEntity donationEntity) {
        donationRepository.delete(donationEntity);
    }

    private void applyRequest(DonationEntity donationEntity, DonationRequest data) {
        donationEntity.setClientId(data.getClientId());
        donationEntity.setName(data.getName());
        donationEntity.setAge(data.getAge());
        donationEntity.setPhone(data.getPhone());
        donationEntity.setCityId(data.getCityId());
        donationEntity.setBloodType(data.getBloodType());
        donationEntity.setComment(data.getComment());
        donationEntity.setAddress(data.getAddress());
        donationEntity.setAmount(data.getAmount());
        donationEntity.setLatitude(data.getLatitude());
        donationEntity.setLongitude(data.getLongitude());
    }

}
