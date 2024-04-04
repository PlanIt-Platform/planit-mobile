package com.example.planit_mobile.services.models

/**
 * Represents the input model for editing a user.
 * @param name The name of the user.
 * @param interests The interests of the user.
 * @param description The description of the user.
 */
data class EditUserInputModel (
    val name: String,
    val interests: List<String>,
    val description: String
)