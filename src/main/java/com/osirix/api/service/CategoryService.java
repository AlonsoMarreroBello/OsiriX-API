package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.dto.category.CategoryResponseDto;

/**
 * Service interface for managing application categories. This interface defines
 * the contract for operations related to categories, such as retrieving,
 * creating, updating, and deleting categories. It uses
 * {@link CategoryRequestDto} for input and {@link CategoryResponseDto} for
 * output.
 *
 * @author Alonso Marrero Bello
 */
public interface CategoryService {

	/**
	 * Retrieves all available categories.
	 *
	 * @return A list of {@link CategoryResponseDto} objects representing all
	 *         categories. Returns an empty list if no categories are found.
	 */
	List<CategoryResponseDto> getAll();

	/**
	 * Retrieves a specific category by its unique identifier.
	 *
	 * @param categoryId The ID of the category to retrieve.
	 * @return The {@link CategoryResponseDto} object for the found category. May
	 *         return {@code null} or throw an exception (e.g.,
	 *         {@code EntityNotFoundException}) if no category with the given ID is
	 *         found, depending on the implementation.
	 */
	CategoryResponseDto getById(Long categoryId);

	/**
	 * Creates a new category.
	 *
	 * @param request The {@link CategoryRequestDto} object containing the data for
	 *                the new category (e.g., name, description).
	 * @return The {@link CategoryResponseDto} object for the newly created
	 *         category. May throw an exception if category creation fails (e.g.,
	 *         validation error, duplicate name).
	 */
	CategoryResponseDto create(CategoryRequestDto request);

	/**
	 * Updates an existing category.
	 *
	 * @param categoryId The ID of the category to update.
	 * @param request    The {@link CategoryRequestDto} object containing the
	 *                   updated data for the category.
	 * @return The {@link CategoryResponseDto} object for the updated category. May
	 *         throw an exception if the category to update is not found or if the
	 *         update fails (e.g., validation error).
	 */
	CategoryResponseDto update(Long categoryId, CategoryRequestDto request);

	/**
	 * Deletes a category by its unique identifier.
	 *
	 * @param categoryId The ID of the category to delete. May throw an exception if
	 *                   the category to delete is not found or if deletion is
	 *                   constrained.
	 */
	void deleteById(Long categoryId);

}
