package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.request.RequestDto;
import com.osirix.api.dto.request.RequestResponseDto;
import com.osirix.api.dto.request.RequestSimpleDto;
import com.osirix.api.service.impl.RequestServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") 
public class RequestController {

    private static final String GENERIC_REQUEST_RESOURCE = "/request";
    private static final String GENERIC_REQUEST_ID_PATH = GENERIC_REQUEST_RESOURCE + "/{requestId}";
    private static final String GENERIC_REQUESTS_BY_USER_PATH = GENERIC_REQUEST_RESOURCE + "/by-user/{userId}";

    @Autowired
    RequestServiceImpl requestService;

    @GetMapping(value = GENERIC_REQUEST_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong generic-requests....");
    }

    @GetMapping(GENERIC_REQUEST_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<RequestResponseDto>>> getAllRequests() {
        List<RequestResponseDto> requests = requestService.getAll();
        ApiResponseDto<List<RequestResponseDto>> response = new ApiResponseDto<>(
                "All generic requests fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GENERIC_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<RequestResponseDto>> getRequestById(@PathVariable Long requestId) {
        RequestResponseDto request = requestService.getById(requestId);
        ApiResponseDto<RequestResponseDto> response = new ApiResponseDto<>(
                "Generic request fetched successfully",
                HttpStatus.OK.value(),
                request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(GENERIC_REQUESTS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<RequestResponseDto>>> getRequestsByUserId(@PathVariable Long userId) {
        List<RequestResponseDto> requests = requestService.getByUserId(userId);
        ApiResponseDto<List<RequestResponseDto>> response = new ApiResponseDto<>(
                "Generic requests for user fetched successfully",
                HttpStatus.OK.value(),
                requests);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(GENERIC_REQUEST_RESOURCE)
    public ResponseEntity<ApiResponseDto<RequestResponseDto>> createRequest(@RequestBody RequestSimpleDto requestSimpleDto) {
        RequestResponseDto createdRequest = requestService.create(requestSimpleDto);
        ApiResponseDto<RequestResponseDto> response = new ApiResponseDto<>(
                "Generic request created successfully",
                HttpStatus.CREATED.value(),
                createdRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(GENERIC_REQUEST_ID_PATH)
    public ResponseEntity<ApiResponseDto<RequestResponseDto>> updateRequest(
            @PathVariable Long requestId,
            @RequestBody RequestDto requestDto) {
        RequestResponseDto updatedRequest = requestService.update(requestId, requestDto);
        ApiResponseDto<RequestResponseDto> response = new ApiResponseDto<>(
                "Generic request updated successfully",
                HttpStatus.OK.value(),
                updatedRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(GENERIC_REQUEST_ID_PATH)
    public ResponseEntity<Void> deleteRequest(@PathVariable Long requestId) {
        requestService.delete(requestId);
        return ResponseEntity.noContent().build();
    }
}