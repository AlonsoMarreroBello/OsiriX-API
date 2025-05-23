package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;

import java.util.List;

//Assuming DTOs are defined elsewhere:
//import com.example.dto.UserResponseDto;
//import com.example.dto.UserRequestDto;

/**
* Service interface for managing user accounts.
* This interface defines the contract for operations related to users,
* such as retrieving user information, updating profiles, deleting users,
* and managing account status (e.g., enabling/disabling, locking/unlocking).
* It uses {@link UserRequestDto} for input and {@link UserResponseDto} for output.
*
* @author Alonso Marrero Bello
*/
public interface UserService {

 /**
  * Retrieves all users in the system, including all types of users (e.g., normal users, staff, administrators).
  *
  * @return A list of {@link UserResponseDto} objects representing all users.
  *         Returns an empty list if no users are found.
  */
 List<UserResponseDto> getAll();

 /**
  * Retrieves all "normal" users, excluding staff or administrative accounts.
  * The definition of a "normal user" depends on the application's role system.
  *
  * @return A list of {@link UserResponseDto} objects representing all normal users.
  *         Returns an empty list if no normal users are found.
  */
 List<UserResponseDto> getNormalUsers();

 /**
  * Retrieves a specific user by their unique identifier.
  *
  * @param userId The ID of the user to retrieve.
  * @return The {@link UserResponseDto} object for the found user.
  *         May return {@code null} or throw an exception if no user with the given ID is found.
  */
 UserResponseDto getUserById(Long userId);

 /**
  * Updates an existing user's profile information.
  *
  * @param userId The ID of the user to update.
  * @param request The {@link UserRequestDto} object containing the updated data for the user.
  * @return The {@link UserResponseDto} object for the updated user.
  *         May throw an exception if the user to update is not found or if the update fails (e.g., validation error).
  */
 UserResponseDto updateUser(Long userId, UserRequestDto request);

 /**
  * Deletes a user by their unique identifier.
  *
  * @param userId The ID of the user to delete.
  *         May throw an exception if the user to delete is not found.
  */
 void deleteUser(Long userId);

 /**
  * Toggles the enabled status of a user's account (e.g., from enabled to disabled or vice-versa).
  * A disabled account cannot log in.
  *
  * @param userId The ID of the user whose enabled status is to be toggled.
  * @return The {@link UserResponseDto} object for the user with their updated enabled status.
  *         May throw an exception if the user is not found.
  */
 UserResponseDto toggleEnable(Long userId);

 /**
  * Toggles the locked status of a user's account (e.g., from locked to unlocked or vice-versa).
  * A locked account cannot log in, often due to security reasons like multiple failed login attempts.
  *
  * @param userId The ID of the user whose account lock status is to be toggled.
  * @return The {@link UserResponseDto} object for the user with their updated account lock status.
  *         May throw an exception if the user is not found.
  */
 UserResponseDto toggleLockAccount(Long userId);

}