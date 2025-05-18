package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.user.publisher.PublisherRequestDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;
import com.osirix.api.service.impl.PublisherServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Publisher Management", description = "APIs for managing publishers")
public class PublisherController {

    private static final String PUBLISHER_RESOURCE = "/publishers";
    private static final String PUBLISHER_ID_PATH = PUBLISHER_RESOURCE + "/{publisherId}";

    @Autowired
    PublisherServiceImpl publisherService;

    @Operation(summary = "Ping Publisher Controller", description = "A simple ping endpoint to check if the Publisher controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = PUBLISHER_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong publishers....");
    }

    @Operation(summary = "Get all publishers", description = "Retrieves a list of all available publishers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of publishers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(PUBLISHER_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<PublisherResponseDto>>> getAllPublishers() {
        List<PublisherResponseDto> publishers = publisherService.getAll();
        ApiResponseDto<List<PublisherResponseDto>> response = new ApiResponseDto<>(
                "Publishers fetched successfully",
                HttpStatus.OK.value(),
                publishers);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get publisher by ID", description = "Retrieves a specific publisher by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved publisher"),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(PUBLISHER_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublisherResponseDto>> getPublisherById(
            @Parameter(description = "ID of the publisher to retrieve", required = true, example = "1") @PathVariable Long publisherId) {
        PublisherResponseDto publisher = publisherService.getPublisherById(publisherId);
        ApiResponseDto<PublisherResponseDto> response = new ApiResponseDto<>(
                "Publisher fetched successfully",
                HttpStatus.OK.value(),
                publisher);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new publisher", description = "Creates a new publisher with the provided details. This typically involves an existing user becoming a publisher.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Publisher created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for publisher creation (e.g., missing user ID or publisher name)"),
            @ApiResponse(responseCode = "404", description = "User to be made a publisher not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - User is already a publisher or publisher name is taken"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(PUBLISHER_RESOURCE)
    public ResponseEntity<ApiResponseDto<PublisherResponseDto>> createPublisher(
            @Valid @RequestBody PublisherRequestDto publisherRequestDto) {
        PublisherResponseDto createdPublisher = publisherService.createPublisher(publisherRequestDto);
        ApiResponseDto<PublisherResponseDto> response = new ApiResponseDto<>(
                "Publisher created successfully",
                HttpStatus.CREATED.value(),
                createdPublisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing publisher", description = "Updates an existing publisher identified by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for publisher update"),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID to update"),
            @ApiResponse(responseCode = "409", description = "Conflict - Updated publisher name might already be taken"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(PUBLISHER_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublisherResponseDto>> updatePublisher(
            @Parameter(description = "ID of the publisher to update", required = true, example = "1") @PathVariable Long publisherId,
            @Valid @RequestBody PublisherRequestDto publisherRequestDto) {
        PublisherResponseDto updatedPublisher = publisherService.updatePublisher(publisherId, publisherRequestDto);
        ApiResponseDto<PublisherResponseDto> response = new ApiResponseDto<>(
                "Publisher updated successfully",
                HttpStatus.OK.value(),
                updatedPublisher);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a publisher", description = "Deletes a publisher by their ID. This may also involve reverting the user's publisher status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Publisher deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Publisher not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(PUBLISHER_ID_PATH)
    public ResponseEntity<Void> deletePublisher(
            @Parameter(description = "ID of the publisher to delete", required = true, example = "1") @PathVariable Long publisherId) {
        publisherService.deletePublisher(publisherId);
        return ResponseEntity.noContent().build();
    }
}