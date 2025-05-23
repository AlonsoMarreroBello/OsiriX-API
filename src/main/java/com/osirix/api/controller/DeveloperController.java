package com.osirix.api.controller;


import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.developer.DeveloperRequestDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;
import com.osirix.api.service.impl.DeveloperServiceImpl;

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
@Tag(name = "Developer Management", description = "APIs for managing developers")
public class DeveloperController {

    private static final String DEVELOPER_RESOURCE = "/developers";
    private static final String DEVELOPER_ID_PATH = DEVELOPER_RESOURCE + "/{developerId}";

    @Autowired
    DeveloperServiceImpl developerService;

    @Operation(summary = "Ping Developer Controller", description = "A simple ping endpoint to check if the Developer controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = DEVELOPER_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong developers....");
    }

    @Operation(summary = "Get all developers", description = "Retrieves a list of all available developers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of developers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(DEVELOPER_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<DeveloperResponseDto>>> getAllDevelopers() {
        List<DeveloperResponseDto> developers = developerService.getAll();
        ApiResponseDto<List<DeveloperResponseDto>> response = new ApiResponseDto<>(
                "Developers fetched successfully",
                HttpStatus.OK.value(),
                developers);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get developer by ID", description = "Retrieves a specific developer by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved developer"),
            @ApiResponse(responseCode = "404", description = "Developer not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(DEVELOPER_ID_PATH)
    public ResponseEntity<ApiResponseDto<DeveloperResponseDto>> getDeveloperById(
            @Parameter(description = "ID of the developer to retrieve", required = true, example = "1") @PathVariable Long developerId) {
        DeveloperResponseDto developer = developerService.getById(developerId);
        ApiResponseDto<DeveloperResponseDto> response = new ApiResponseDto<>(
                "Developer fetched successfully",
                HttpStatus.OK.value(),
                developer);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new developer", description = "Creates a new developer with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Developer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for developer creation (e.g., missing name or email)"),
            @ApiResponse(responseCode = "409", description = "Conflict - A developer with the same email or name might already exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(DEVELOPER_RESOURCE)
    public ResponseEntity<ApiResponseDto<DeveloperResponseDto>> createDeveloper(
            @Valid @RequestBody DeveloperRequestDto developerRequestDto) {
        DeveloperResponseDto createdDeveloper = developerService.createDeveloper(developerRequestDto);
        ApiResponseDto<DeveloperResponseDto> response = new ApiResponseDto<>(
                "Developer created successfully",
                HttpStatus.CREATED.value(),
                createdDeveloper);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing developer", description = "Updates an existing developer identified by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Developer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for developer update"),
            @ApiResponse(responseCode = "404", description = "Developer not found with the given ID to update"),
            @ApiResponse(responseCode = "409", description = "Conflict - A developer with the updated email or name might already exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(DEVELOPER_ID_PATH)
    public ResponseEntity<ApiResponseDto<DeveloperResponseDto>> updateDeveloper(
            @Parameter(description = "ID of the developer to update", required = true, example = "1") @PathVariable Long developerId,
            @Valid @RequestBody DeveloperRequestDto developerRequestDto) {
        DeveloperResponseDto updatedDeveloper = developerService.updateDeveloper(developerId, developerRequestDto);
        ApiResponseDto<DeveloperResponseDto> response = new ApiResponseDto<>(
                "Developer updated successfully",
                HttpStatus.OK.value(),
                updatedDeveloper);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a developer", description = "Deletes a developer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Developer deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Developer not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(DEVELOPER_ID_PATH)
    public ResponseEntity<Void> deleteDeveloper(
            @Parameter(description = "ID of the developer to delete", required = true, example = "1") @PathVariable Long developerId) {
        developerService.deleteDeveloper(developerId);
        return ResponseEntity.noContent().build();
    }
}