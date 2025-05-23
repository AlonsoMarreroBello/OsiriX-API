package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.request.publication.PublicationRequestDto;
import com.osirix.api.dto.request.publication.PublicationRequestSimpleDto;
import com.osirix.api.dto.request.publication.PublicationResponseDto;

/**
 * Service interface for managing application publication requests. This
 * interface defines the contract for operations related to the lifecycle of
 * publication requests, such as creation, retrieval, updating, and deletion.
 * Publication requests are initiated by users wanting to publish
 * their applications and may require review or approval by
 * staff/administrators.
 *
 * @author Alonso Marrero Bello
 */
public interface PublicationRequestService {

	/**
	 * Retrieves all publication requests submitted by a specific user.
	 *
	 * @param userId The ID of the user whose publication requests are to be
	 *               retrieved.
	 * @return A list of {@link PublicationResponseDto} objects representing the
	 *         user's publication requests. Returns an empty list if the user has no
	 *         publication requests.
	 */
	List<PublicationResponseDto> getByUserId(Long userId);

	/**
	 * Retrieves all publication requests assigned to a specific staff member (e.g.,
	 * an administrator or reviewer).
	 *
	 * @param adminId The ID of the staff member to whom the requests are assigned.
	 * @return A list of {@link PublicationResponseDto} objects representing
	 *         publication requests assigned to the staff member. Returns an empty
	 *         list if no requests are assigned to this staff member.
	 */
	List<PublicationResponseDto> getByAssignedStaffId(Long adminId);

	/**
	 * Retrieves a specific publication request by its unique identifier.
	 *
	 * @param requestId The ID of the publication request to retrieve.
	 * @return The {@link PublicationResponseDto} object for the found publication
	 *         request. May return {@code null} or throw an exception if no request
	 *         with the given ID is found.
	 */
	PublicationResponseDto getById(Long requestId);

	/**
	 * Creates a new publication request. This uses a simplified DTO for
	 * initial creation.
	 *
	 * @param request The {@link PublicationRequestSimpleDto} object containing the
	 *                initial data for the new publication request.
	 * @return The {@link PublicationResponseDto} object for the newly created
	 *         publication request. May throw an exception if request creation fails
	 *         (e.g., validation error).
	 */
	PublicationResponseDto create(PublicationRequestSimpleDto request);

	/**
	 * Updates an existing publication request. This might involve changing its
	 * status, adding comments, or assigning it to a staff member.
	 *
	 * @param requestId The ID of the publication request to update.
	 * @param request   The {@link PublicationRequestDto} object containing the
	 *                  updated data for the publication request.
	 * @return The {@link PublicationResponseDto} object for the updated publication
	 *         request. May throw an exception if the request to update is not found
	 *         or if the update fails.
	 */
	PublicationResponseDto update(Long requestId, PublicationRequestDto request);

	/**
	 * Deletes a publication request by its unique identifier. This might be used to
	 * cancel a request or remove a completed/rejected one from the system.
	 *
	 * @param requestId The ID of the publication request to delete. May throw an
	 *                  exception if the request to delete is not found.
	 */
	void delete(Long requestId);

}