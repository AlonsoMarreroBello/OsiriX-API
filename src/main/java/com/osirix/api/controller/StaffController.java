package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.role.RoleResponseDto;
import com.osirix.api.dto.user.staff.StaffRequestDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;
import com.osirix.api.service.impl.StaffServiceImpl;

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
@Tag(name = "Staff Management", description = "APIs for managing staff members")
public class StaffController {

    private static final String STAFF_RESOURCE = "/staff";
    private static final String ROLE_RESOURCE = "/roles";    
    private static final String STAFF_ID_PATH = STAFF_RESOURCE + "/{staffId}";

    @Autowired
    StaffServiceImpl staffService;

    @Operation(summary = "Ping Staff Controller", description = "A simple ping endpoint to check if the Staff controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = STAFF_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong staff....");
    }

    @Operation(summary = "Get all staff members", description = "Retrieves a list of all staff members.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of staff members"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(STAFF_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<StaffResponseDto>>> getAllStaff() {
        List<StaffResponseDto> staffList = staffService.getAll();
        ApiResponseDto<List<StaffResponseDto>> response = new ApiResponseDto<>(
                "All staff fetched successfully",
                HttpStatus.OK.value(),
                staffList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new staff member", description = "Creates a new staff member from an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Staff member created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for staff creation (e.g., missing user ID or role)"),
            @ApiResponse(responseCode = "404", description = "User to be made a staff member not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - User is already a staff member"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(STAFF_RESOURCE)
    public ResponseEntity<ApiResponseDto<StaffResponseDto>> createStaff(
            @Valid @RequestBody StaffRequestDto staffRequestDto) {
        StaffResponseDto createdStaff = staffService.createStaff(staffRequestDto);
        ApiResponseDto<StaffResponseDto> response = new ApiResponseDto<>(
                "Staff created successfully",
                HttpStatus.CREATED.value(),
                createdStaff);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing staff member", description = "Updates an existing staff member's details, such as their role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff member updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for staff update"),
            @ApiResponse(responseCode = "404", description = "Staff member not found with the given ID to update"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(STAFF_ID_PATH)
    public ResponseEntity<ApiResponseDto<StaffResponseDto>> updateStaff(
            @Parameter(description = "ID of the staff member to update", required = true, example = "1") @PathVariable Long staffId,
            @Valid @RequestBody StaffRequestDto staffRequestDto) {
        StaffResponseDto updatedStaff = staffService.update(staffId, staffRequestDto);
        ApiResponseDto<StaffResponseDto> response = new ApiResponseDto<>(
                "Staff updated successfully",
                HttpStatus.OK.value(),
                updatedStaff);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(ROLE_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<RoleResponseDto>>> getAllRoles() {
    	List<RoleResponseDto> roles = staffService.getAllRoles();
        ApiResponseDto<List<RoleResponseDto>> response = new ApiResponseDto<>(
                "Roles fetched successfully",
                HttpStatus.OK.value(),
                roles);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
    
}