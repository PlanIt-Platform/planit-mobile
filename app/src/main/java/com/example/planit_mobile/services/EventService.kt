package com.example.planit_mobile.services

interface EventService {

    /**
     * Creates a new event with the provided information.
     * @param title The title of the new event.
     * @param description The description of the new event.
     * @param category The category of the new event.
     * @param subcategory The subcategory of the new event.
     * @param location The location of the new event.
     * @param visibility The visibility of the new event.
     * @param date The date of the new event.
     * @param endDate The end date of the new event.
     * @param price The price of the new event.
     * @param userID The ID of the user creating the event.
     * @return containing the ID of the newly created event, its title and a status message.
     */
    suspend fun createEvent(
        title: String,
        description: String?,
        category: String,
        subcategory: String?,
        location: String?,
        visibility: String?,
        date: String?,
        endDate: String?,
        price: String?,
        userID: Int
    )

    /**
     * Retrieves the event associated with the given ID.
     * @param eventID The ID of the event to retrieve.
     * @return The event associated with the ID.
     */
    suspend fun fetchEventInfo(eventID: Int)

    /**
     * Retrieves the users in the event associated with the given ID.
     * @param eventID The ID of the event to retrieve the users from.
     * @return The users in the event associated with the ID.
     */

    suspend fun getUsersInEvent(eventID: Int)
}