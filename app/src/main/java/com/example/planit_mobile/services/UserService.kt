package com.example.planit_mobile.services

import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.models.AuthOutputModel

/**
 * Interface for interacting with user-related operations in the PlanIt application.
 */
interface UserService {

    /**
     * Registers a new user in the PlanIt application.
     *
     * @param username The username of the user.
     * @param name The name of the user.
     * @param email The email address of the user.
     * @param password The password for the user account.
     * @return The authentication tokens for the newly registered user.
     */
    suspend fun register(
        username: String,
        name: String,
        email: String,
        password: String
    ): AuthOutputModel

    /**
     * Logs in a user with the provided email and password.
     *
     * @param emailOrName The email address or the name of the user.
     * @param password The password for the user account.
     * @return The authentication tokens for the logged-in user.
     */
    suspend fun login(emailOrName: String, password: String): AuthOutputModel

    /**
     * Logs out the user with the specified access and refresh tokens.
     *
     * @param accessToken The access token for authentication.
     * @param refreshToken The refresh token for obtaining a new access token.
     */
    suspend fun logout(accessToken: String, refreshToken: String)

    /**
     * Fetches information about a specific user.
     *
     * @param uID The unique identifier of the user.
     * @param userAccessToken The access token for the user.
     * @param userRefreshToken The refresh token for the user.
     * @return The details of the requested user.
     */
    suspend fun fetchUserInfo(uID: Int, userAccessToken: String, userRefreshToken: String): User

    /**
     * Edits the interests and description of a user.
     *
     * @param userAccessToken The access token for the user.
     * @param userRefreshToken The refresh token for the user.
     * @param name The name of the user.
     * @param interests The interests of the user.
     * @param description The description of the user.
     */
    suspend fun editUser(
        userAccessToken: String,
        userRefreshToken: String,
        name: String,
        interests: List<String>,
        description: String
    )
}
