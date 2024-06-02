package com.example.planit_mobile.services.providers

import android.util.Log
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.models.CreateEventInputModel
import com.example.planit_mobile.services.models.CreateEventOutputModel
import com.example.planit_mobile.services.models.CreatePollOutputModel
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.services.models.PollModel
import com.example.planit_mobile.services.models.SearchEventsResult
import com.example.planit_mobile.services.models.SuccessMessage
import com.example.planit_mobile.services.models.UsersInEventResult
import com.example.planit_mobile.services.utils.ApiRequests
import com.example.planit_mobile.services.utils.PathTemplates.CREATE_EVENT
import com.example.planit_mobile.services.utils.PathTemplates.GET_CATEGORIES
import com.example.planit_mobile.services.utils.PathTemplates.getAssignUserTaskPath
import com.example.planit_mobile.services.utils.PathTemplates.getCreatePollPath
import com.example.planit_mobile.services.utils.PathTemplates.getDeleteEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getEditEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getJoinEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getKickUserFromEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getLeaveEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getPollPath
import com.example.planit_mobile.services.utils.PathTemplates.getPollsPath
import com.example.planit_mobile.services.utils.PathTemplates.getRemoveUserTaskPath
import com.example.planit_mobile.services.utils.PathTemplates.getSubcategoriesPath
import com.example.planit_mobile.services.utils.PathTemplates.getUsersInEventPath
import com.example.planit_mobile.services.utils.PathTemplates.getVotePollPath
import com.example.planit_mobile.services.utils.PathTemplates.searchEventPath
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient

class EventProvider(
    private val client: OkHttpClient,
    private val gson: Gson,
): EventService {
    override suspend fun createEvent(
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
    ): CreateEventOutputModel {
        val data = CreateEventInputModel(
            title,
            description,
            category,
            subcategory,
            location,
            visibility,
            date,
            endDate,
            price,
            password
        )
        return ApiRequests(client, gson).postRequest(
            CREATE_EVENT,
            gson.toJson(data),
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun fetchEventInfo(
        eventID: Int,
        userAccessToken: String,
        userRefreshToken: String
    ): EventModel = ApiRequests(client, gson).getRequest(
            getEventPath(eventID),
            userAccessToken,
            userRefreshToken
        )

    override suspend fun getUsersInEvent(
        eventID: Int,
        accessToken: String,
        refreshToken: String
    ): UsersInEventResult = ApiRequests(client, gson).getRequest(
            getUsersInEventPath(eventID),
            accessToken,
            refreshToken
        )

    override suspend fun getCategories(): List<String>
        = ApiRequests(client, gson).getRequest(GET_CATEGORIES)

    override suspend fun getSubcategories(category: String): List<String> =
        ApiRequests(client, gson).getRequest(getSubcategoriesPath(category))

    override suspend fun searchEvents(
        userAccessToken: String,
        userRefreshToken: String,
        query: String?,
        limit: Int?,
        offset: Int?
    ): SearchEventsResult =
        ApiRequests(client, gson).getRequest(
            searchEventPath(query, limit, offset), userAccessToken, userRefreshToken
        )

    override suspend fun joinEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int,
        password: String
    ): SuccessMessage =
        ApiRequests(client, gson).postRequest(
            getJoinEventPath(eventID),
            "{\"password\":\"$password\"}",
            userAccessToken,
            userRefreshToken
        )

    override suspend fun leaveEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int
    ): SuccessMessage = ApiRequests(client, gson).postRequest(
            getLeaveEventPath(eventID),
        "{}",
            accessToken = userAccessToken,
            refreshToken = userRefreshToken
        )

    override suspend fun deleteEvent(
        userAccessToken: String,
        userRefreshToken: String,
        eventID: Int
    ): SuccessMessage = ApiRequests(client, gson).deleteRequest(
            getDeleteEventPath(eventID),
            userAccessToken,
            userRefreshToken
        )

    override suspend fun editEvent(
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
    ): SuccessMessage {
        return ApiRequests(client, gson).putRequest(
            getEditEventPath(eventID),
            "{" +
                    "\"title\":\"$title\"," +
                    if(description != null)"\"description\":\"$description\"," else "" +
                    "\"category\":\"$category\"," +
                    if(subcategory != null)"\"subcategory\":\"$subcategory\"," else "" +
                    "\"location\":\"$location\"," +
                    "\"visibility\":\"$visibility\"," +
                    "\"date\":\"$date\"," +
                    if(endDate != null) "\"endDate\":\"$endDate\"," else "" +
                    "\"price\":\"$price\"," +
                    "\"password\":\"$password\"" +
                "}",
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun removeUserTask(
        userAccessToken: String,
        userRefreshToken: String,
        userId: Int,
        eventId: Int,
        taskId: Int
    ): SuccessMessage {
        return ApiRequests(client, gson).deleteRequest(
            getRemoveUserTaskPath(userId, eventId, taskId),
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun assignUserTask(
        userAccessToken: String,
        userRefreshToken: String,
        userId: Int,
        eventId: Int,
        taskName: String
    ): SuccessMessage {
        return ApiRequests(client, gson).postRequest(
            getAssignUserTaskPath(userId, eventId),
            "{\"taskName\":\"$taskName\"}",
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun kickUserFromEvent(
        userAccessToken: String,
        userRefreshToken: String,
        userId: Int,
        eventId: Int
    ): SuccessMessage {
        return ApiRequests(client, gson).deleteRequest(
            getKickUserFromEventPath(eventId, userId),
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun getPolls(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int
    ): List<PollModel> {
        val pollsJson: List<Map<String, Any>> = ApiRequests(client, gson).getRequest(
            getPollsPath(eventId),
            userAccessToken,
            userRefreshToken
        )

        val pollList: List<PollModel> = pollsJson.map { pollMap ->
            val pollJson = gson.toJson(pollMap)
            gson.fromJson(pollJson, PollModel::class.java)
        }

        return pollList
    }

    override suspend fun createPoll(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int,
        title: String,
        duration: String,
        options: List<String>
    ): CreatePollOutputModel {
        return ApiRequests(client, gson).postRequest(
            getCreatePollPath(eventId),
            "{\"title\":\"$title\"," +
                    "\"duration\":\"$duration\"," +
                    "\"options\":${gson.toJson(options)}}",
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun deletePoll(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int,
        pollId: Int
    ): SuccessMessage {
        return ApiRequests(client, gson).deleteRequest(
            getPollPath(eventId, pollId),
            userAccessToken,
            userRefreshToken
        )
    }

    override suspend fun voteOnPoll(
        userAccessToken: String,
        userRefreshToken: String,
        eventId: Int,
        pollId: Int,
        optionId: Int
    ): SuccessMessage {
        return ApiRequests(client, gson).putRequest(
            getVotePollPath(eventId, pollId, optionId),
            "{}",
            userAccessToken,
            userRefreshToken
        )
    }

}