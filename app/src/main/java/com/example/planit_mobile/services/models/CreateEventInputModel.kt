package com.example.planit_mobile.services.models

/**
 * Represents the input model for creating an event.
 * @param title The title of the event.
 * @param description The description of the event.
 * @param category The category of the event.
 * @param subcategory The subcategory of the event.
 * @param location The location of the event.
 * @param visibility The visibility of the event.
 * @param date The date of the event.
 * @param endDate The end date of the event.
 * @param price The price of the event.
 * @param password The password of the event.
 */
data class CreateEventInputModel(
    val title: String,
    val description: String?,
    val category: String,
    val subCategory: String,
    val location: String?,
    val visibility: String?,
    val date: String,
    val endDate: String?,
    val price: String,
    val password: String
)
