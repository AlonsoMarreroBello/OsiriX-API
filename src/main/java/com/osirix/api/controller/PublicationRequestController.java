package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.request.publication.PublicationRequestDto;
import com.osirix.api.dto.request.publication.PublicationRequestSimpleDto;
import com.osirix.api.dto.request.publication.PublicationResponseDto;
import com.osirix.api.service.impl.PublicationRequestServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PublicationRequestController {

    private static final String PUBLICATION_REQUEST_RESOURCE = "/requests/publication";
    private static final String PUBLICATION_REQUEST_ID_PATH = PUBLICATION_REQUEST_RESOURCE + "/{requestId}";
    private static final String REQUESTS_BY_USER_PATH = PUBLICATION_REQUEST_RESOURCE + "/by-user/{userId}";
    private static final String REQUESTS_BY_STAFF_PATH = PUBLICATION_REQUEST_RESOURCE + "/by-staff/{staffId}";

    @Autowired
    PublicationRequestServiceImpl publicationRequestService;

    @GetMapping(value = PUBLICATION_REQUEST_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong publication-requests....");
    }

    @GetMapping(REQUESTS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<PublicationResponseDto>>> getPublicationRequestsByUserId(@PathVariable Long userId) {
        List<PublicationResponseDto> requests = publicationRequestService.getByUserId(userId);
        ApiResponseDto<List<PublicationResponseDto>> response = new ApiResponseDto<>(
                "Publication requests for user fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(REQUESTS_BY_STAFF_PATH)
    public ResponseEntity<ApiResponseDto<List<PublicationResponseDto>>> getPublicationRequestsByAssignedStaffId(@PathVariable Long staffId) {
        List<PublicationResponseDto> requests = publicationRequestService.getByAssignedStaffId(staffId);
        ApiResponseDto<List<PublicationResponseDto>> response = new ApiResponseDto<>(
                "Publication requests for staff member fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(PUBLICATION_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<PublicationResponseDto>> getPublicationRequestById(@PathVariable Long requestId) {
        PublicationResponseDto request = publicationRequestService.getById(requestId);
        ApiResponseDto<PublicationResponseDto> response = new ApiResponseDto<>(
                "Publication request fetched successfully",
                HttpStatus.OK.value(),
                request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(PUBLICATION_REQUEST_RESOURCE)
    public ResponseEntity<ApiResponseDto<PublicationResponseDto>> createPublicationRequest(@RequestBody PublicationRequestSimpleDto requestSimpleDto) {
        PublicationResponseDto createdRequest = publicationRequestService.create(requestSimpleDto);
        ApiResponseDto<PublicationResponseDto> response = new ApiResponseDto<>(
                "Publication request created successfully",
                HttpStatus.CREATED.value(),
                createdRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(PUBLICATION_REQUEST_ID_PATH) 
    public ResponseEntity<ApiResponseDto<PublicationResponseDto>> updatePublicationRequest(
            @PathVariable Long requestId,
            @RequestBody PublicationRequestDto requestDto) {
        PublicationResponseDto updatedRequest = publicationRequestService.update(requestId, requestDto);
        ApiResponseDto<PublicationResponseDto> response = new ApiResponseDto<>(
                "Publication request updated successfully",
                HttpStatus.OK.value(),
                updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(PUBLICATION_REQUEST_ID_PATH)
    public ResponseEntity<Void> deletePublicationRequest(@PathVariable Long requestId) {
        publicationRequestService.delete(requestId);
        return ResponseEntity.noContent().build();
    }
}