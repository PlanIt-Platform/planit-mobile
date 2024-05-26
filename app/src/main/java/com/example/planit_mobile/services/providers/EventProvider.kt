package com.example.planit_mobile.services.providers

import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.models.CreateEventInputModel
import com.example.planit_mobile.services.models.CreateEventOutputModel
import com.example.planit_mobile.services.utils.ApiRequests
import com.example.planit_mobile.services.utils.PathTemplates.CREATE_EVENT
import com.example.planit_mobile.services.utils.PathTemplates.GET_CATEGORIES
import com.example.planit_mobile.services.utils.PathTemplates.getSubcategoriesPath
import com.google.gson.Gson
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

    override suspend fun fetchEventInfo(eventID: Int) {}

    override suspend fun getUsersInEvent(eventID: Int) {}

    override suspend fun getCategories(): List<String>
        = ApiRequests(client, gson).getRequest(GET_CATEGORIES)

    override suspend fun getSubcategories(category: String): List<String> =
        ApiRequests(client, gson).getRequest(getSubcategoriesPath(category))
}