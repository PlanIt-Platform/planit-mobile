package com.example.planit_mobile.services.models

/**
 * Represents the output model for creating an event.
 *
 * @param id The ID of the event.
 * @param title The title of the event.
 * @param status The status of the event.
 */
data class CreateEventOutputModel(
    val id: Int,
    val title: String,
    val status: String
)
