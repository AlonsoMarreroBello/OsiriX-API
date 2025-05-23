package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.user.publisher.PublisherRequestDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;

/**
 * Service interface for managing publisher entities. Publishers are typically
 * organizations or individuals responsible for publishing applications. This
 * interface defines the contract for operations related to publishers, such as
 * retrieving, creating, updating, and deleting publisher profiles. It generally
 * uses {@link PublisherRequestDto} for input and {@link PublisherResponseDto}
 * for output.
 *
 * @author Alonso Marrero Bello
 */
public interface PublisherService {

	/**
	 * Retrieves all registered publishers.
	 *
	 * @return A list of {@link PublisherResponseDto} objects representing all
	 *         publishers. Returns an empty list if no publishers are found.
	 */
	List<PublisherResponseDto> getAll();

	/**
	 * Retrieves a specific publisher by their unique identifier.
	 *
	 * @param publisherId The ID of the publisher to retrieve.
	 * @return The {@link PublisherResponseDto} object for the found publisher. May
	 *         return {@code null} or throw an exception if no publisher with the
	 *         given ID is found.
	 */
	PublisherResponseDto getPublisherById(Long publisherId);

	/**
	 * Creates a new publisher profile.
	 *
	 * @param request The {@link PublisherRequestDto} object containing the data for
	 *                the new publisher.
	 * @return The {@link PublisherResponseDto} object for the newly created
	 *         publisher. May throw an exception if publisher creation fails (e.g.,
	 *         validation error, duplicate name).
	 */
	PublisherResponseDto createPublisher(PublisherRequestDto request);

	/**
	 * Updates an existing publisher's profile.
	 *
	 * @param publisherId The ID of the publisher to update.
	 * @param request     The {@link PublisherRequestDto} object containing the
	 *                    updated data for the publisher.
	 * @return The {@link PublisherResponseDto} object for the updated publisher.
	 *         May throw an exception if the publisher to update is not found or if
	 *         the update fails.
	 */
	PublisherResponseDto updatePublisher(Long publisherId, PublisherRequestDto request);

	/**
	 * Deletes a publisher by their unique identifier.
	 *
	 * @param publisherId The ID of the publisher to delete. May throw an exception
	 *                    if the publisher to delete is not found.
	 */
	void deletePublisher(Long publisherId);

}