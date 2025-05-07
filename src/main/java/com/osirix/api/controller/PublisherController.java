package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.user.publisher.PublisherRequestDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;
import com.osirix.api.service.impl.PublisherServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") 
public class PublisherController {

    private static final String PUBLISHER_RESOURCE = "/publishers";
    private static final String PUBLISHER_ID_PATH = PUBLISHER_RESOURCE + "/{publisherId}";

    @Autowired
    PublisherServiceImpl publisherService;

    @GetMapping(value = PUBLISHER_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong publishers....");
    }

    @GetMapping(PUBLISHER_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<PublisherResponseDto>>> getAllPublishers() {
        List<PublisherResponseDto> publishers = publisherService.getAll();
        ApiResponseDto<List<PublisherResponseDto>> response = new ApiResponseDto<>(
                "Publishers fetched successfully",
                HttpStatus.OK.value(),
                publishers);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(PUBLISHER_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublisherResponseDto>> getPublisherById(@PathVariable Long publisherId) {
        PublisherResponseDto publisher = publisherService.getPublisherById(publisherId);
        ApiResponseDto<PublisherResponseDto> response = new ApiResponseDto<>(
                "Publisher fetched successfully",
                HttpStatus.OK.value(),
                publisher);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(PUBLISHER_RESOURCE)
    public ResponseEntity<ApiResponseDto<PublisherResponseDto>> createPublisher(@RequestBody PublisherRequestDto publisherRequestDto) {
        PublisherResponseDto createdPublisher = publisherService.createPublisher(publisherRequestDto);
        ApiResponseDto<PublisherResponseDto> response = new ApiResponseDto<>(
                "Publisher created successfully",
                HttpStatus.CREATED.value(),
                createdPublisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(PUBLISHER_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublisherResponseDto>> updatePublisher(
            @PathVariable Long publisherId,
            @RequestBody PublisherRequestDto publisherRequestDto) {
        PublisherResponseDto updatedPublisher = publisherService.updatePublisher(publisherId, publisherRequestDto);
        ApiResponseDto<PublisherResponseDto> response = new ApiResponseDto<>(
                "Publisher updated successfully",
                HttpStatus.OK.value(),
                updatedPublisher);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(PUBLISHER_ID_PATH)
    public ResponseEntity<Void> deletePublisher(@PathVariable Long publisherId) {
        publisherService.deletePublisher(publisherId);
        return ResponseEntity.noContent().build();
    }
}