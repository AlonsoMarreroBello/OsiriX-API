package com.osirix.api.service;

import com.osirix.api.dto.auth.AuthLoginRequestDto;
import com.osirix.api.dto.auth.AuthResponseDto;
import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service interface for handling authentication and authorization processes.
 * This interface extends Spring Security's {@link UserDetailsService} to
 * integrate with its authentication mechanisms, and also defines custom methods
 * for application-specific authentication, registration, and permission checks.
 *
 * @author Alonso Marrero Bello
 */
public interface AuthService extends UserDetailsService {

	/**
	 * Checks if the currently authenticated user has permission to upload content
	 * or updates related to a specific application.
	 *
	 * @param appId The ID of the application to check permissions for.
	 * @return {@code true} if the user is authorized to upload to the app,
	 *         {@code false} otherwise.
	 */
	boolean canUserUploadToApp(Long appId);

	/**
	 * Checks if the currently authenticated user has permission to download a
	 * specific application. This might involve checking ownership, purchase status,
	 * or general access rights.
	 *
	 * @param appId The ID of the application to check download permissions for.
	 * @return {@code true} if the user is authorized to download the app,
	 *         {@code false} otherwise.
	 */
	boolean canUserDownloadApp(Long appId);

	/**
	 * Authenticates a user based on the provided login credentials. Upon successful
	 * authentication, it returns an authentication token or session
	 * information.
	 *
	 * @param request The {@link AuthLoginRequestDto} object containing the user's
	 *                login credentials (e.g., username/email and password).
	 * @return An {@link AuthResponseDto} object containing authentication details
	 *         (e.g., JWT token, user information) upon successful login. May throw
	 *         an authentication-related exception if login fails.
	 */
	AuthResponseDto login(AuthLoginRequestDto request);

	/**
	 * Registers a new user in the system.
	 *
	 * @param request The {@link UserRequestDto} object containing the details for
	 *                the new user (e.g., username, password, email).
	 * @return An {@link AuthResponseDto} object, which might contain a token for
	 *         the newly registered and authenticated user, or a confirmation
	 *         message. May throw an exception if registration fails (e.g., username
	 *         already exists).
	 */
	AuthResponseDto register(UserRequestDto request);

	/**
	 * Authenticates a user directly using their username and password. This method
	 * is used for programmatic authentication or when integrating with
	 * Spring Security's authentication mechanisms more directly.
	 *
	 * @param name     The username (or email, depending on configuration) of the
	 *                 user.
	 * @param password The plain text password of the user.
	 * @return An {@link Authentication} object representing the authenticated
	 *         principal if successful. This object will have its
	 *         {@code isAuthenticated()} method return {@code true}. May throw an
	 *         {@code AuthenticationException} if authentication fails.
	 * @see org.springframework.security.authentication.AuthenticationManager#authenticate(Authentication)
	 */
	Authentication authenticate(String name, String password);

}