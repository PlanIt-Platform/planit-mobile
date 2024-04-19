package com.example.planit_mobile.domain

/**
 * Represents an event.
 * @property id The unique identifier of the event.
 * @property title The title of the event.
 * @property description The description of the event.
 * @property date The date of the event.
 * @property category The category of the event.
 * @property subCategory The subcategory of the event.
 * @property location The location of the event.
 * @property visibility The visibility of the event.
 * @property price The price of the event.
 */
data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val category: String,
    val subCategory: String,
    val location: String,
    val visibility: String,
    val price: Double
)