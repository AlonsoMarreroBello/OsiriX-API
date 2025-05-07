package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.dto.category.CategoryResponseDto;
import com.osirix.api.service.impl.CategoryServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private static final String CATEGORY_RESOURCE = "/categories";
    private static final String CATEGORY_ID_PATH = CATEGORY_RESOURCE + "/{categoryId}";

    @Autowired
    CategoryServiceImpl categoryService;

    @GetMapping(value = CATEGORY_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong categories....");
    }

    @GetMapping(CATEGORY_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAll();
        ApiResponseDto<List<CategoryResponseDto>> response = new ApiResponseDto<>(
                "Categories fetched successfully",
                HttpStatus.OK.value(),
                categories);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(CATEGORY_ID_PATH)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> getCategoryById(@PathVariable Long categoryId) {
        CategoryResponseDto category = categoryService.getById(categoryId);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>(
                "Category fetched successfully",
                HttpStatus.OK.value(),
                category);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(CATEGORY_RESOURCE)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto createdCategory = categoryService.create(categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>(
                "Category created successfully",
                HttpStatus.CREATED.value(),
                createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(CATEGORY_ID_PATH)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.update(categoryId, categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>(
                "Category updated successfully",
                HttpStatus.OK.value(),
                updatedCategory);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(CATEGORY_ID_PATH)
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) { 
        categoryService.deleteById(categoryId);
        return ResponseEntity.noContent().build(); 
    }
}