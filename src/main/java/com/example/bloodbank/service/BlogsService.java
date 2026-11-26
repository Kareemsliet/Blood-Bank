package com.example.bloodbank.service;

import com.example.bloodbank.dto.BlogDto;
import com.example.bloodbank.dto.request.BlogRequest;
import com.example.bloodbank.dto.response.PaginationResponse;
import com.example.bloodbank.entity.BlogEntity;
import com.example.bloodbank.exception.NotFoundIDException;
import com.example.bloodbank.repository.BlogRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class BlogsService {

    private final BlogRepository blogRepository;

    public BlogEntity findById(long id){
        return blogRepository.findById(id).orElseThrow(
                () -> new NotFoundIDException("Blog with id " + id + " not found")
        );
    }

    public PaginationResponse getPagination(int pageNumber, String query){
        pageNumber = Math.max(pageNumber,1);
        int pageIndex = pageNumber - 1;
        int pageSize = 12;
        Page<BlogEntity> pagination = this.blogRepository.findByTitleContaining(
                query,
                PageRequest.of(pageIndex,pageSize,Sort.by("createdAt").descending())
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

    public BlogDto getById(long id){
        return BlogDto.toDto(this.findById(id));
    }

    public void create(@Valid BlogRequest data){
        BlogEntity blogEntity = new BlogEntity();
        applyRequest(blogEntity,data);
        blogEntity.setCreatedAt(LocalDateTime.now());
        this.blogRepository.save(blogEntity);
    }

    public void update(BlogEntity blogEntity, @Valid BlogRequest data){
        applyRequest(blogEntity,data);
        blogEntity.setUpdatedAt(LocalDateTime.now());
        this.blogRepository.save(blogEntity);
    }

    private void applyRequest(BlogEntity blogEntity, BlogRequest data) {
        blogEntity.setTitle(data.getTitle());
        blogEntity.setContent(data.getContent());
        blogEntity.setCategoryId(data.getCategoryId());
    }

    public void uploadImage(BlogEntity blogEntity,MultipartFile image) throws IOException {
        String storageDir = "src/main/resources/uploads/blogs";
        Path storagePath = Path.of(storageDir).toAbsolutePath().normalize();
        if(Files.notExists(storagePath)){
            Files.createDirectories(storagePath);
        }

        if(blogEntity.getImage() != null){
            Files.deleteIfExists(storagePath.resolve(blogEntity.getImage()));
        }

        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null;
        String newFile = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        Path newPath = storagePath.resolve(newFile);

        Files.copy(image.getInputStream(),newPath);

        blogEntity.setImage(newFile);
        blogEntity.setUpdatedAt(LocalDateTime.now());

        this.blogRepository.save(blogEntity);
    }

    public void delete(BlogEntity blogEntity) throws IOException {
        Path storagePath = Path.of("src/main/resources/uploads/blogs").toAbsolutePath().normalize();
        Files.deleteIfExists(storagePath.resolve(blogEntity.getImage()));
        this.blogRepository.delete(blogEntity);
    }

}
