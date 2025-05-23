package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.request.publication.PublicationRequestDto;
import com.osirix.api.dto.request.publication.PublicationRequestSimpleDto;
import com.osirix.api.dto.request.publication.PublicationResponseDto;
import com.osirix.api.service.impl.PublicationRequestServiceImpl;

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
@Tag(name = "Publication Request Management", description = "APIs for managing publication requests")
public class PublicationRequestController {

    private static final String PUBLICATION_REQUEST_RESOURCE = "/requests/publication";
    private static final String PUBLICATION_REQUEST_ID_PATH = PUBLICATION_REQUEST_RESOURCE + "/{requestId}";
    private static final String REQUESTS_BY_USER_PATH = PUBLICATION_REQUEST_RESOURCE + "/by-user/{userId}";
    private static final String REQUESTS_BY_STAFF_PATH = PUBLICATION_REQUEST_RESOURCE + "/by-staff/{staffId}";

    @Autowired
    PublicationRequestServiceImpl publicationRequestService;

    @Operation(summary = "Ping Publication Request Controller", description = "A simple ping endpoint to check if the Publication Request controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = PUBLICATION_REQUEST_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong publication-requests....");
    }

    @Operation(summary = "Get publication requests by user ID", description = "Retrieves all publication requests submitted by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved publication requests for the user"),
            @ApiResponse(responseCode = "404", description = "User not found or user has no publication requests"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(REQUESTS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<PublicationResponseDto>>> getPublicationRequestsByUserId(
            @Parameter(description = "ID of the user whose publication requests are to be retrieved", required = true, example = "101") @PathVariable Long userId) {
        List<PublicationResponseDto> requests = publicationRequestService.getByUserId(userId);
        ApiResponseDto<List<PublicationResponseDto>> response = new ApiResponseDto<>(
                "Publication requests for user fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get publication requests by assigned staff ID", description = "Retrieves all publication requests assigned to a specific staff member.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved publication requests for the staff member"),
            @ApiResponse(responseCode = "404", description = "Staff member not found or has no assigned publication requests"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(REQUESTS_BY_STAFF_PATH)
    public ResponseEntity<ApiResponseDto<List<PublicationResponseDto>>> getPublicationRequestsByAssignedStaffId(
            @Parameter(description = "ID of the staff member whose assigned publication requests are to be retrieved", required = true, example = "202") @PathVariable Long staffId) {
        List<PublicationResponseDto> requests = publicationRequestService.getByAssignedStaffId(staffId);
        ApiResponseDto<List<PublicationResponseDto>> response = new ApiResponseDto<>(
                "Publication requests for staff member fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get publication request by ID", description = "Retrieves a specific publication request by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved publication request"),
            @ApiResponse(responseCode = "404", description = "Publication request not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(PUBLICATION_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublicationResponseDto>> getPublicationRequestById(
            @Parameter(description = "ID of the publication request to retrieve", required = true, example = "1") @PathVariable Long requestId) {
        PublicationResponseDto request = publicationRequestService.getById(requestId);
        ApiResponseDto<PublicationResponseDto> response = new ApiResponseDto<>(
                "Publication request fetched successfully",
                HttpStatus.OK.value(),
                request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new publication request", description = "Creates a new publication request with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Publication request created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for publication request creation (e.g., missing required fields)"),
            @ApiResponse(responseCode = "404", description = "Associated user or app not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(PUBLICATION_REQUEST_RESOURCE)
    public ResponseEntity<ApiResponseDto<PublicationResponseDto>> createPublicationRequest(
            @Valid @RequestBody PublicationRequestSimpleDto requestSimpleDto) {
        PublicationResponseDto createdRequest = publicationRequestService.create(requestSimpleDto);
        ApiResponseDto<PublicationResponseDto> response = new ApiResponseDto<>(
                "Publication request created successfully",
                HttpStatus.CREATED.value(),
                createdRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing publication request", description = "Updates an existing publication request identified by its ID. This can be used to change status, assign staff, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publication request updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for publication request update"),
            @ApiResponse(responseCode = "404", description = "Publication request, user, or staff member not found with the given ID(s)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(PUBLICATION_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublicationResponseDto>> updatePublicationRequest(
            @Parameter(description = "ID of the publication request to update", required = true, example = "1") @PathVariable Long requestId,
            @Valid @RequestBody PublicationRequestDto requestDto) {
        PublicationResponseDto updatedRequest = publicationRequestService.update(requestId, requestDto);
        ApiResponseDto<PublicationResponseDto> response = new ApiResponseDto<>(
                "Publication request updated successfully",
                HttpStatus.OK.value(),
                updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a publication request", description = "Deletes a publication request by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Publication request deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Publication request not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(PUBLICATION_REQUEST_ID_PATH)
    public ResponseEntity<Void> deletePublicationRequest(
            @Parameter(description = "ID of the publication request to delete", required = true, example = "1") @PathVariable Long requestId) {
        publicationRequestService.delete(requestId);
        return ResponseEntity.noContent().build();
    }
}