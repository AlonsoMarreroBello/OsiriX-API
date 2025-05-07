package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.user.staff.StaffRequestDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;
import com.osirix.api.service.impl.StaffServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StaffController {

    private static final String STAFF_RESOURCE = "/staff";
    private static final String STAFF_ID_PATH = STAFF_RESOURCE + "/{staffId}";

    @Autowired
    StaffServiceImpl staffService;

    @GetMapping(value = STAFF_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong staff....");
    }

    @GetMapping(STAFF_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<StaffResponseDto>>> getAllStaff() {
        List<StaffResponseDto> staffList = staffService.getAll();
        ApiResponseDto<List<StaffResponseDto>> response = new ApiResponseDto<>(
                "All staff fetched successfully",
                HttpStatus.OK.value(),
                staffList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(STAFF_RESOURCE)
    public ResponseEntity<ApiResponseDto<StaffResponseDto>> createStaff(@RequestBody StaffRequestDto staffRequestDto) {
        StaffResponseDto createdStaff = staffService.createStaff(staffRequestDto);
        ApiResponseDto<StaffResponseDto> response = new ApiResponseDto<>(
                "Staff created successfully",
                HttpStatus.CREATED.value(),
                createdStaff);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(STAFF_ID_PATH)
    public ResponseEntity<ApiResponseDto<StaffResponseDto>> updateStaff(
            @PathVariable Long staffId,
            @RequestBody StaffRequestDto staffRequestDto) {
        StaffResponseDto updatedStaff = staffService.update(staffId, staffRequestDto);
        ApiResponseDto<StaffResponseDto> response = new ApiResponseDto<>(
                "Staff updated successfully",
                HttpStatus.OK.value(),
                updatedStaff);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}