package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.friendship.FriendshipResponseDto;

/**
 * Service interface for managing user friendships. This interface defines the
 * contract for operations related to friendships, such as retrieving friendship
 * details, sending requests, and managing friendship statuses. It typically
 * uses {@link FriendshipResponseDto} for output.
 *
 * @author Alonso Marrero Bello
 */
public interface FriendshipService {

	/**
	 * Retrieves a specific friendship by its unique identifier.
	 *
	 * @param id The ID of the friendship to retrieve.
	 * @return The {@link FriendshipResponseDto} object for the found friendship.
	 *         May return {@code null} or throw an exception if no friendship with
	 *         the given ID is found.
	 */
	FriendshipResponseDto getById(Long id);

	/**
	 * Retrieves all friendships associated with a specific user.
	 *
	 * @param userId The ID of the user whose friendships are to be retrieved.
	 * @return A list of {@link FriendshipResponseDto} objects representing the
	 *         user's friendships. Returns an empty list if the user has no
	 *         friendships.
	 */
	List<FriendshipResponseDto> getByUserId(Long userId);

	/**
	 * Sends a friendship request from one user to another. The target user is
	 * identified by their username.
	 *
	 * @param senderId The ID of the user sending the friendship request.
	 * @param username The username of the user to whom the friendship request is
	 *                 being sent.
	 * @return The {@link FriendshipResponseDto} object representing the newly
	 *         created friendship request. May throw an exception if the request
	 *         cannot be sent (e.g., target user not found, request already exists).
	 */
	FriendshipResponseDto sendFriendshipRequest(Long senderId, String username);

	/**
	 * Accepts a pending friendship request. This method changes the
	 * status of an existing friendship record.
	 *
	 * @param friendshipId The ID of the friendship request to accept. May throw an
	 *                     exception if the friendship request is not found or
	 *                     cannot be accepted (e.g., not pending).
	 */
	void acceptFriendship(Long friendshipId);

	/**
	 * Deletes or cancels a friendship or a friendship request. This could mean
	 * declining a request, un-friending, or withdrawing a sent request.
	 *
	 * @param friendshipId The ID of the friendship or friendship request to delete.
	 *                     May throw an exception if the friendship is not found.
	 */
	void deleteFriendship(Long friendshipId);

}