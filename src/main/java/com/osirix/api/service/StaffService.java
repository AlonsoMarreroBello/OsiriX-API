package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.role.RoleResponseDto;
import com.osirix.api.dto.user.staff.StaffRequestDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;

import java.util.List;


/**
 * Service interface for managing staff members and their roles within the
 * system. This interface defines the contract for operations related to staff,
 * such as retrieving staff information, creating new staff members, updating
 * existing ones, and listing available roles. It uses
 * {@link StaffRequestDto} for input and {@link StaffResponseDto} or
 * {@link RoleResponseDto} for output.
 *
 * @author Alonso Marrero Bello
 */
public interface StaffService {

	/**
	 * Retrieves all staff members in the system.
	 *
	 * @return A list of {@link StaffResponseDto} objects representing all staff
	 *         members. Returns an empty list if no staff members are found.
	 */
	List<StaffResponseDto> getAll();

	/**
	 * Creates a new staff member profile. This involves assigning roles
	 * and permissions.
	 *
	 * @param request The {@link StaffRequestDto} object containing the data for the
	 *                new staff member.
	 * @return The {@link StaffResponseDto} object for the newly created staff
	 *         member. May throw an exception if staff member creation fails (e.g.,
	 *         validation error, user already exists).
	 */
	StaffResponseDto createStaff(StaffRequestDto request);

	/**
	 * Updates an existing staff member's profile or roles.
	 *
	 * @param staffId The ID of the staff member to update. This ID usually
	 *                corresponds to a user ID.
	 * @param request The {@link StaffRequestDto} object containing the updated data
	 *                for the staff member.
	 * @return The {@link StaffResponseDto} object for the updated staff member. May
	 *         throw an exception if the staff member to update is not found or if
	 *         the update fails.
	 */
	StaffResponseDto update(Long staffId, StaffRequestDto request);

	/**
	 * Retrieves all available roles that can be assigned to staff members or users.
	 *
	 * @return A list of {@link RoleResponseDto} objects representing all available
	 *         roles in the system. Returns an empty list if no roles are defined.
	 */
	List<RoleResponseDto> getAllRoles();

}