package com.example.bloodbank.dto.response;

import java.util.List;

public class PaginationResponse extends MainResponse {
    public PaginationResponse (
            List<?> items,
            int currentPage,
            int lastPage,
            boolean hasNext,
            boolean hasPrevious,
            long total
    ){
        super(
                true,
                null,
                new data(
                    items,
                    currentPage,
                    lastPage,
                    hasNext,
                    hasPrevious,
                    total
                )
        );
    }
    private record data(
            List<?> items,
            int currentPage,
            int lastPage,
            boolean hasNext,
            boolean hasPrevious,
            long total
    ) {}
}
