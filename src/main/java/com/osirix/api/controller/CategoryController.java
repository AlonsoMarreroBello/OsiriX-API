package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.dto.category.CategoryResponseDto;
import com.osirix.api.service.impl.CategoryServiceImpl;

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
@Tag(name = "Category Management", description = "APIs for managing categories")
public class CategoryController {

    private static final String CATEGORY_RESOURCE = "/categories";
    private static final String CATEGORY_ID_PATH = CATEGORY_RESOURCE + "/{categoryId}";

    @Autowired
    CategoryServiceImpl categoryService;

    @Operation(summary = "Ping Category Controller", description = "A simple ping endpoint to check if the Category controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = CATEGORY_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong categories....");
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all available categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(CATEGORY_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAll();
        ApiResponseDto<List<CategoryResponseDto>> response = new ApiResponseDto<>(
                "Categories fetched successfully",
                HttpStatus.OK.value(),
                categories);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved category"),
            @ApiResponse(responseCode = "404", description = "Category not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(CATEGORY_ID_PATH)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> getCategoryById(
            @Parameter(description = "ID of the category to retrieve", required = true, example = "1") @PathVariable Long categoryId) {
        CategoryResponseDto category = categoryService.getById(categoryId);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>(
                "Category fetched successfully",
                HttpStatus.OK.value(),
                category);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new category", description = "Creates a new category with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for category creation (e.g., missing name)"),
            @ApiResponse(responseCode = "409", description = "Conflict - A category with the same name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(CATEGORY_RESOURCE)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> createCategory(
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto createdCategory = categoryService.create(categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>(
                "Category created successfully",
                HttpStatus.CREATED.value(),
                createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing category", description = "Updates an existing category identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for category update"),
            @ApiResponse(responseCode = "404", description = "Category not found with the given ID to update"),
            @ApiResponse(responseCode = "409", description = "Conflict - A category with the updated name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(CATEGORY_ID_PATH)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> updateCategory(
            @Parameter(description = "ID of the category to update", required = true, example = "1") @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.update(categoryId, categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>(
                "Category updated successfully",
                HttpStatus.OK.value(),
                updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Category not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(CATEGORY_ID_PATH)
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true, example = "1") @PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }
}