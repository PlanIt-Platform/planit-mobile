package com.example.planit_mobile.domain

/**
 * Represents a user in the PlanIt application.
 *
 * @property id The unique identifier for the user.
 * @property name The name of the user.
 * @property username The username of the user.
 * @property description The description of the user.
 * @property email The email address of the user.
 * @property interests The list of interests associated with the user.
 */
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val description: String,
    val email: String,
    val interests: List<String>
)
