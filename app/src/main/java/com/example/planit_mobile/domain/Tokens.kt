package com.example.planit_mobile.domain

/**
 * Represents a pair of access and refresh tokens used for authentication.
 *
 * @property accessToken The access token for authentication.
 * @property refreshToken The refresh token for obtaining a new access token.
 */
data class Tokens(
    val accessToken: String,
    val refreshToken: String,
)
