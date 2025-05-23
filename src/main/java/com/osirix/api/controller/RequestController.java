package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.request.RequestDto;
import com.osirix.api.dto.request.RequestResponseDto;
import com.osirix.api.dto.request.RequestSimpleDto;
import com.osirix.api.service.impl.RequestServiceImpl;

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
@Tag(name = "Generic Request Management", description = "APIs for managing generic user requests")
public class RequestController {

    private static final String GENERIC_REQUEST_RESOURCE = "/request";
    private static final String GENERIC_REQUEST_ID_PATH = GENERIC_REQUEST_RESOURCE + "/{requestId}";
    private static final String GENERIC_REQUESTS_BY_USER_PATH = GENERIC_REQUEST_RESOURCE + "/by-user/{userId}";

    @Autowired
    RequestServiceImpl requestService;

    @Operation(summary = "Ping Generic Request Controller", description = "A simple ping endpoint to check if the Generic Request controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = GENERIC_REQUEST_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong generic-requests....");
    }

    @Operation(summary = "Get all generic requests", description = "Retrieves a list of all generic requests in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all generic requests"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(GENERIC_REQUEST_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<RequestResponseDto>>> getAllRequests() {
        List<RequestResponseDto> requests = requestService.getAll();
        ApiResponseDto<List<RequestResponseDto>> response = new ApiResponseDto<>(
                "All generic requests fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get generic request by ID", description = "Retrieves a specific generic request by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved generic request"),
            @ApiResponse(responseCode = "404", description = "Generic request not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(GENERIC_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<RequestResponseDto>> getRequestById(
            @Parameter(description = "ID of the generic request to retrieve", required = true, example = "1") @PathVariable Long requestId) {
        RequestResponseDto request = requestService.getById(requestId);
        ApiResponseDto<RequestResponseDto> response = new ApiResponseDto<>(
                "Generic request fetched successfully",
                HttpStatus.OK.value(),
                request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get generic requests by user ID", description = "Retrieves all generic requests submitted by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved generic requests for the user"),
            @ApiResponse(responseCode = "404", description = "User not found or user has no generic requests"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(GENERIC_REQUESTS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<RequestResponseDto>>> getRequestsByUserId(
            @Parameter(description = "ID of the user whose generic requests are to be retrieved", required = true, example = "101") @PathVariable Long userId) {
        List<RequestResponseDto> requests = requestService.getByUserId(userId);
        ApiResponseDto<List<RequestResponseDto>> response = new ApiResponseDto<>(
                "Generic requests for user fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new generic request", description = "Creates a new generic request with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Generic request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for generic request creation (e.g., missing required fields)"),
            @ApiResponse(responseCode = "404", description = "Associated user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(GENERIC_REQUEST_RESOURCE)
    public ResponseEntity<ApiResponseDto<RequestResponseDto>> createRequest(
            @Valid @RequestBody RequestSimpleDto requestSimpleDto) {
        RequestResponseDto createdRequest = requestService.create(requestSimpleDto);
        ApiResponseDto<RequestResponseDto> response = new ApiResponseDto<>(
                "Generic request created successfully",
                HttpStatus.CREATED.value(),
                createdRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing generic request", description = "Updates an existing generic request identified by its ID. This can be used to change its status, content, or assigned staff member.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Generic request updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for generic request update"),
            @ApiResponse(responseCode = "404", description = "Generic request, user, or staff member not found with the given ID(s)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(GENERIC_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<RequestResponseDto>> updateRequest(
            @Parameter(description = "ID of the generic request to update", required = true, example = "1") @PathVariable Long requestId,
            @Valid @RequestBody RequestDto requestDto) {
        RequestResponseDto updatedRequest = requestService.update(requestId, requestDto);
        ApiResponseDto<RequestResponseDto> response = new ApiResponseDto<>(
                "Generic request updated successfully",
                HttpStatus.OK.value(),
                updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a generic request", description = "Deletes a generic request by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Generic request deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Generic request not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(GENERIC_REQUEST_ID_PATH)
    public ResponseEntity<Void> deleteRequest(
            @Parameter(description = "ID of the generic request to delete", required = true, example = "1") @PathVariable Long requestId) {
        requestService.delete(requestId);
        return ResponseEntity.noContent().build();
    }
}