package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.app.AppRequestDto;
import com.osirix.api.dto.app.AppResponseDto;

import java.util.List;

/**
 * Service interface for managing application entities. This interface defines
 * the contract for operations related to applications, such as retrieval,
 * creation, updating, deletion, and other specific business logic. It typically
 * uses {@link AppRequestDto} for input and {@link AppResponseDto} for output.
 *
 * @author Alonso Marrero Bello
 */
public interface AppService {

	/**
	 * Retrieves all applications.
	 *
	 * @return A list of {@link AppResponseDto} objects representing all
	 *         applications.
	 */
	List<AppResponseDto> getAll();

	/**
	 * Retrieves a stack of applications.
	 *
	 * @param appId The ID of the application for which to retrieve the following
	 *              stack.
	 * @return A list of {@link AppResponseDto} the next stack of apps.
	 */
	List<AppResponseDto> getStack(Long appId);

	/**
	 * Retrieves a specific application by its unique identifier.
	 *
	 * @param appId The ID of the application to retrieve.
	 * @return The {@link AppResponseDto} object for the found application, or
	 *         null/throws exception if not found.
	 */
	AppResponseDto getById(Long appId);

	/**
	 * Retrieves applications whose names partially match the given string. The
	 * search is case-insensitive.
	 *
	 * @param name The partial name to search for.
	 * @return A list of {@link AppResponseDto} objects for applications matching
	 *         the partial name.
	 */
	List<AppResponseDto> getByPartialName(String name);

	/**
	 * Retrieves all applications published by a specific publisher.
	 *
	 * @param publisherId The ID of the publisher.
	 * @return A list of {@link AppResponseDto} objects for applications from the
	 *         specified publisher.
	 */
	List<AppResponseDto> getAppsByPublisher(Long publisherId);

	/**
	 * Retrieves all applications developed by a specific developer.
	 *
	 * @param developerId The ID of the developer.
	 * @return A list of {@link AppResponseDto} objects for applications from the
	 *         specified developer.
	 */
	List<AppResponseDto> getAppsByDeveloper(Long developerId);

	/**
	 * Retrieves all applications belonging to any of the specified categories.
	 *
	 * @param categoryId A list of category IDs.
	 * @return A list of {@link AppResponseDto} objects for applications within the
	 *         given categories.
	 */
	List<AppResponseDto> getAppsByCategory(List<Long> categoryId);

	/**
	 * Retrieves all applications associated with a specific user (e.g., in their
	 * library).
	 *
	 * @param userId The ID of the user.
	 * @return A list of {@link AppResponseDto} objects associated with the
	 *         specified user.
	 */
	List<AppResponseDto> getAppsByUserId(Long userId);

	/**
	 * Creates a new application.
	 *
	 * @param request The {@link AppRequestDto} object containing the data for the
	 *                new application.
	 * @return The {@link AppResponseDto} object for the newly created application.
	 */
	AppResponseDto create(AppRequestDto request);

	/**
	 * Updates an existing application.
	 *
	 * @param appId   The ID of the application to update.
	 * @param request The {@link AppRequestDto} object containing the updated data.
	 * @return The {@link AppResponseDto} object for the updated application.
	 */
	AppResponseDto update(Long appId, AppRequestDto request);

	/**
	 * Toggles the published status of an application (e.g., from published to
	 * unpublished or vice-versa).
	 *
	 * @param appId The ID of the application whose publish status is to be toggled.
	 * @return The {@link AppResponseDto} object for the application with its
	 *         updated publish status.
	 */
	AppResponseDto togglePublish(Long appId);

	/**
	 * Toggles the visibility status of an application (e.g., shown or hidden in
	 * listings).
	 *
	 * @param appId The ID of the application whose visibility status is to be
	 *              toggled.
	 * @return The {@link AppResponseDto} object for the application with its
	 *         updated visibility status.
	 */
	AppResponseDto toggleShow(Long appId);

	/**
	 * Toggles the download availability status of an application.
	 *
	 * @param appId The ID of the application whose download status is to be
	 *              toggled.
	 * @return The {@link AppResponseDto} object for the application with its
	 *         updated download status.
	 */
	AppResponseDto toggleDownload(Long appId);

	/**
	 * Deletes an application by its unique identifier.
	 *
	 * @param appId The ID of the application to delete.
	 */
	void deleteById(Long appId);

	/**
	 * Checks if an application with the given ID exists.
	 *
	 * @param id The ID of the application to check.
	 * @return {@code true} if the application exists, {@code false} otherwise.
	 */
	boolean appExists(Long id);

	/**
	 * Retrieves the stored filename for the application's binary or package. This
	 * could be a path or a key in a storage system.
	 *
	 * @param appId The ID of the application.
	 * @return The stored filename or path for the application.
	 */
	String getAppStoredFilename(Long appId);

	/**
	 * Adds a specific application to a specific user's library.
	 *
	 * @param userId The ID of the user.
	 * @param appId  The ID of the application to add.
	 * @return {@code true} if the application was successfully added, {@code false}
	 *         otherwise (e.g., already in library).
	 */
	boolean addAppToUserLibrary(Long userId, Long appId);

	/**
	 * Checks if a specific user is the publisher of a specific application.
	 *
	 * @param userId The ID of the user.
	 * @param appId  The ID of the application.
	 * @return {@code true} if the user is the publisher of the app, {@code false}
	 *         otherwise.
	 */
	boolean isUserPublisherOfApp(Long userId, Long appId);

}
