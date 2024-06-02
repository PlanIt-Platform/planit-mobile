package com.example.planit_mobile.services

import com.example.planit_mobile.services.models.CreateEventOutputModel
import com.example.planit_mobile.services.models.CreatePollOutputModel
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.services.models.PollModel
import com.example.planit_mobile.services.models.PollOption
import com.example.planit_mobile.services.models.SearchEventsResult
import com.example.planit_mobile.services.models.SuccessMessage
import com.example.planit_mobile.services.models.UsersInEventResult

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
        userAccessToken: String,
        userRefreshToken: String,
        title: String,
        description: String?,
        category: String,
        subcategory: String,
        location: String?,
        visibility: String?,
        date: String,
        endDate: String?,
        price: String,
        password: String
    ) : CreateEventOutputModel

    /**
     * Retrieves the event associated with the given ID.
     * @param eventID The ID of the event to retrieve.
     * @return The event associated with the ID.
     */
    suspend fun fetchEventInfo(
        eventID: Int,
        userAccessToken: String,
        userRefreshToken: String
    ) : EventModel

    /**
     * Retrieves the users in the event associated with the given ID.
     * @param eventID The ID of the event to retrieve the users from.
     * @return The users in the event associated with the ID.
     */

    suspend fun getUsersInEvent(
        eventID: Int,
        accessToken: String,
        refreshToken: String
    ): UsersInEventResult

    /**
     * Retrieves the categories of events available in the application.
     * @return The categories of events available in the application.
     */
    suspend fun getCategories(): List<String>

    /**
     * Retrieves the subcategories of events available in the application.
     * @return The subcategories of events available in the application.
     */
    suspend fun getSubcategories(category: String): List<String>

    /**
     * Searches for events based on the provided query.
     * @param query The query to search for.
     * @param limit The maximum number of events to return.
     * @param offset The number of events to skip.
     * @return The events that match the query.
      */
    suspend fun searchEvents(
        userAccessToken: String,
        userRefreshToken: String,
        query: String?,
        limit: Int?,
        offset: Int?
    ) : SearchEventsResult

    /**
     * Joins the event associated with the given ID.
     * @param userAccessToken The access token of the user joining the event.
     * @param userRefreshToken The refresh token of the user joining the event.
     * @param eventID The ID of the event to join.
     * @param password The password of the event to join.
     * @return A message indicating the success of the operation.
     */
    suspend fun joinEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int,
        password: String
    ): SuccessMessage

    /**
     * Leaves the event associated with the given ID.
     * @param userAccessToken The access token of the user leaving the event.
     * @param userRefreshToken The refresh token of the user leaving the event.
     * @param eventID The ID of the event to leave.
     * @return A message indicating the success of the operation.
     */
    suspend fun leaveEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int
    ): SuccessMessage

    /**
     * Deletes the event associated with the given ID.
     * @param userAccessToken The access token of the user deleting the event.
     * @param userRefreshToken The refresh token of the user deleting the event.
     * @param eventID The ID of the event to delete.
     * @return A message indicating the success of the operation.
     */
    suspend fun deleteEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int
    ): SuccessMessage

    suspend fun editEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int,
        title: String,
        description: String?,
        category: String,
        subcategory: String?,
        location: String?,
        visibility: String,
        date: String,
        endDate: String?,
        price: String,
        password: String
    ): SuccessMessage

    suspend fun removeUserTask(
        userAccessToken: String,
        userRefreshToken: String,
        userId: Int,
        eventId: Int,
        taskId: Int
    ): SuccessMessage

    suspend fun assignUserTask(
        userAccessToken: String,
        userRefreshToken: String,
        userId: Int,
        eventId: Int,
        taskName: String
    ): SuccessMessage

    suspend fun kickUserFromEvent(
        userAccessToken: String,
        userRefreshToken: String,
        userId: Int,
        eventId: Int
    ): SuccessMessage

    suspend fun getPolls(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int
    ): List<PollModel>

    suspend fun createPoll(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int,
        title: String,
        duration: String,
        options: List<String>
    ): CreatePollOutputModel

    suspend fun deletePoll(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int,
        pollId: Int
    ): SuccessMessage

    suspend fun voteOnPoll(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int,
        pollId: Int,
        optionId: Int
    ): SuccessMessage

}