package com.example.bloodbank.service;

import com.example.bloodbank.dto.BlogDto;
import com.example.bloodbank.dto.DonationDto;
import com.example.bloodbank.dto.request.DonationRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.ClientEntity;
import com.example.bloodbank.entity.BlogEntity;
import com.example.bloodbank.entity.DonationEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.BlogRepository;
import com.example.bloodbank.repository.ClientRepository;
import com.example.bloodbank.repository.DonationRepository;
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
public class ClientAppService {

    private final ClientRepository clientRepository;
    private final DonationRepository donationRepository;
    private final BlogRepository blogRepository;


    public PaginationResponse myDonations(long clientId,int pageNumber) {
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<DonationEntity> pagination = donationRepository.findByClientId(
                clientId,
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

    public DonationDto myDonation(long id,long clientId){
        return DonationDto.toDto(findDonation(id,clientId));
    }

    public void newDonation(@Valid DonationRequest data) {
        DonationEntity donation = new DonationEntity();
        applyDonationRequest(donation,data);
        donationRepository.save(donation);
    }

    public void deleteDonation(DonationEntity donationEntity) {
        donationRepository.delete(donationEntity);
    }

    public DonationEntity findDonation(long id,long clientId){
        return donationRepository.findByIdAndClientId(id,clientId).orElseThrow(
                () -> new NotFoundIDException("Donation with id " + id + " not found")
        );
    }

    public PaginationResponse favouriteBlogs(long clientId,int pageNumber) {
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<BlogEntity> pagination = clientRepository.findBlogFavouritesByClientId(
                clientId,
                PageRequest.of(pageIndex, pageSize, Sort.by("createdAt").descending())
        );
        List<BlogDto> itemsPage = pagination.getContent().stream()
                .map(BlogDto::toDto)
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

    @Transactional
    public void toggleBlog(long clientId,long blogId) {
        BlogEntity blog = blogRepository.findById(blogId).orElseThrow(
                () -> new NotFoundIDException("Blog with id " + blogId + " not found")
        );
        ClientEntity client = clientRepository.findById(clientId).orElseThrow();
        client.toggleBlog(blog);
        clientRepository.save(client);
    }

    private void applyDonationRequest(DonationEntity donationEntity,DonationRequest data) {
        donationEntity.setName(data.getName());
        donationEntity.setClientId(data.getClientId());
        donationEntity.setAge(data.getAge());
        donationEntity.setPhone(data.getPhone());
        donationEntity.setCityId(data.getCityId());
        donationEntity.setBloodType(data.getBloodType());
        donationEntity.setComment(data.getComment());
        donationEntity.setAddress(data.getAddress());
        donationEntity.setAmount(data.getAmount());
        donationEntity.setLatitude(data.getLatitude());
        donationEntity.setLongitude(data.getLongitude());
        donationEntity.setCreatedAt(LocalDateTime.now());
    }

}
