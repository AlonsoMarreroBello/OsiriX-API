package com.osirix.api.controller;


import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.developer.DeveloperRequestDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;
import com.osirix.api.service.impl.DeveloperServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DeveloperController {

    private static final String DEVELOPER_RESOURCE = "/developers";
    private static final String DEVELOPER_ID_PATH = DEVELOPER_RESOURCE + "/{developerId}";

    @Autowired
    DeveloperServiceImpl developerService;

    @GetMapping(value = DEVELOPER_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong developers....");
    }

    @GetMapping(DEVELOPER_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<DeveloperResponseDto>>> getAllDevelopers() {
        List<DeveloperResponseDto> developers = developerService.getAll();
        ApiResponseDto<List<DeveloperResponseDto>> response = new ApiResponseDto<>(
                "Developers fetched successfully",
                HttpStatus.OK.value(),
                developers);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(DEVELOPER_ID_PATH)
    public ResponseEntity<ApiResponseDto<DeveloperResponseDto>> getDeveloperById(@PathVariable Long developerId) {
        DeveloperResponseDto developer = developerService.getById(developerId);
        ApiResponseDto<DeveloperResponseDto> response = new ApiResponseDto<>(
                "Developer fetched successfully",
                HttpStatus.OK.value(),
                developer);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(DEVELOPER_RESOURCE)
    public ResponseEntity<ApiResponseDto<DeveloperResponseDto>> createDeveloper(@RequestBody DeveloperRequestDto developerRequestDto) {
        DeveloperResponseDto createdDeveloper = developerService.createDeveloper(developerRequestDto);
        ApiResponseDto<DeveloperResponseDto> response = new ApiResponseDto<>(
                "Developer created successfully",
                HttpStatus.CREATED.value(),
                createdDeveloper);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(DEVELOPER_ID_PATH)
    public ResponseEntity<ApiResponseDto<DeveloperResponseDto>> updateDeveloper(
            @PathVariable Long developerId,
            @RequestBody DeveloperRequestDto developerRequestDto) {
        DeveloperResponseDto updatedDeveloper = developerService.updateDeveloper(developerId, developerRequestDto);
        ApiResponseDto<DeveloperResponseDto> response = new ApiResponseDto<>(
                "Developer updated successfully",
                HttpStatus.OK.value(),
                updatedDeveloper);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(DEVELOPER_ID_PATH)
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Long developerId) {
        developerService.deleteDeveloper(developerId);
        return ResponseEntity.noContent().build();
    }
}