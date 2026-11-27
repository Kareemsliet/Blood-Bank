package com.example.bloodbank.service;

import com.example.bloodbank.dto.BlogDto;
import com.example.bloodbank.dto.DonationDto;
import com.example.bloodbank.dto.response.PaginationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppService {

    private final BlogsService blogsService;
    private final BlogCategoriesService blogCategoriesService;
    private final StatesService statesService;
    private final CitiesService citiesService;
    private final DonationsService donationsService;

    public PaginationResponse getBlogs(int pageNumber, String query) {
        return blogsService.getPagination(pageNumber, query);
    }

    public BlogDto getBlog(long id) {
        return blogsService.getById(id);
    }

    public PaginationResponse getBlogCategories(int pageNumber, String query) {
        return blogCategoriesService.getPagination(pageNumber, query);
    }

    public PaginationResponse getStates(int pageNumber, String query) {
        return statesService.getPagination(pageNumber, query);
    }

    public PaginationResponse getCities(int pageNumber, String query) {
        return citiesService.getPagination(pageNumber, query);
    }

    public PaginationResponse getDonations(int pageNumber) {
        return donationsService.getPublicPagination(pageNumber);
    }

    public DonationDto getDonation(long id) {
        return donationsService.getById(id);
    }

}
