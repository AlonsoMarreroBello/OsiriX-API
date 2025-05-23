package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.request.RequestDto;
import com.osirix.api.dto.request.RequestResponseDto;
import com.osirix.api.dto.request.RequestSimpleDto;

/**
 * Service interface for managing generic requests within the system. This
 * interface defines the contract for operations related to various types of
 * requests, such as creation, retrieval, updating, and deletion. The specific
 * nature of these requests would be determined by the application's business
 * logic. It uses {@link RequestSimpleDto} or {@link RequestDto} for
 * input and {@link RequestResponseDto} for output.
 *
 * @author Alonso Marrero Bello
 */
public interface RequestService {

	/**
	 * Retrieves all requests in the system.
	 *
	 * @return A list of {@link RequestResponseDto} objects representing all
	 *         requests. Returns an empty list if no requests are found.
	 */
	List<RequestResponseDto> getAll();

	/**
	 * Retrieves a specific request by its unique identifier.
	 *
	 * @param requestId The ID of the request to retrieve.
	 * @return The {@link RequestResponseDto} object for the found request. May
	 *         return {@code null} or throw an exception if no request with the
	 *         given ID is found.
	 */
	RequestResponseDto getById(Long requestId);

	/**
	 * Retrieves all requests submitted by or associated with a specific user.
	 *
	 * @param userId The ID of the user whose requests are to be retrieved.
	 * @return A list of {@link RequestResponseDto} objects representing the user's
	 *         requests. Returns an empty list if the user has no associated
	 *         requests.
	 */
	List<RequestResponseDto> getByUserId(Long userId);

	/**
	 * Creates a new request. This method uses a simplified DTO for the
	 * initial creation of a request.
	 *
	 * @param request The {@link RequestSimpleDto} object containing the initial
	 *                data for the new request.
	 * @return The {@link RequestResponseDto} object for the newly created request.
	 *         May throw an exception if request creation fails (e.g., validation
	 *         error).
	 */
	RequestResponseDto create(RequestSimpleDto request);

	/**
	 * Updates an existing request. This might involve changing its status, content,
	 * or assignments.
	 *
	 * @param requestId The ID of the request to update.
	 * @param request   The {@link RequestDto} object containing the updated data
	 *                  for the request.
	 * @return The {@link RequestResponseDto} object for the updated request. May
	 *         throw an exception if the request to update is not found or if the
	 *         update fails.
	 */
	RequestResponseDto update(Long requestId, RequestDto request);

	/**
	 * Deletes a request by its unique identifier.
	 *
	 * @param requestId The ID of the request to delete. May throw an exception if
	 *                  the request to delete is not found.
	 */
	void delete(Long requestId);

}
