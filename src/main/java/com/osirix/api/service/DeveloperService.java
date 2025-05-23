package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.developer.DeveloperRequestDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;

/**
 * Service interface for managing developer entities. This interface defines the
 * contract for operations related to developers, such as retrieving, creating,
 * updating, and deleting developer profiles. It uses
 * {@link DeveloperRequestDto} for input and {@link DeveloperResponseDto} for
 * output.
 *
 * @author Alonso Marrero Bello
 */
public interface DeveloperService {

	/**
	 * Retrieves all registered developers.
	 *
	 * @return A list of {@link DeveloperResponseDto} objects representing all
	 *         developers. Returns an empty list if no developers are found.
	 */
	List<DeveloperResponseDto> getAll();

	/**
	 * Retrieves a specific developer by their unique identifier.
	 *
	 * @param developerId The ID of the developer to retrieve.
	 * @return The {@link DeveloperResponseDto} object for the found developer. May
	 *         return {@code null} or throw an exception if no developer with the
	 *         given ID is found.
	 */
	DeveloperResponseDto getById(Long developerId);

	/**
	 * Creates a new developer profile.
	 *
	 * @param request The {@link DeveloperRequestDto} object containing the data for
	 *                the new developer.
	 * @return The {@link DeveloperResponseDto} object for the newly created
	 *         developer. May throw an exception if developer creation fails (e.g.,
	 *         validation error).
	 */
	DeveloperResponseDto createDeveloper(DeveloperRequestDto request);

	/**
	 * Updates an existing developer's profile.
	 *
	 * @param developerId The ID of the developer to update.
	 * @param request     The {@link DeveloperRequestDto} object containing the
	 *                    updated data for the developer.
	 * @return The {@link DeveloperResponseDto} object for the updated developer.
	 *         May throw an exception if the developer to update is not found or if
	 *         the update fails.
	 */
	DeveloperResponseDto updateDeveloper(Long developerId, DeveloperRequestDto request);

	/**
	 * Deletes a developer by their unique identifier.
	 *
	 * @param developerId The ID of the developer to delete. May throw an exception
	 *                    if the developer to delete is not found.
	 */
	void deleteDeveloper(Long developerId);

}