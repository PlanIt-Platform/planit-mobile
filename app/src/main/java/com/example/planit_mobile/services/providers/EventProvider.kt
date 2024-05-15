package com.example.planit_mobile.services.providers

import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.utils.ApiRequests
import com.example.planit_mobile.services.utils.PathTemplates.GET_CATEGORIES
import com.google.gson.Gson
import okhttp3.OkHttpClient

class EventProvider(
    private val client: OkHttpClient,
    private val gson: Gson,
): EventService {
    override suspend fun createEvent(
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
    ) {}

    override suspend fun fetchEventInfo(eventID: Int) {}

    override suspend fun getUsersInEvent(eventID: Int) {}

    override suspend fun getCategories(): List<String>
        = ApiRequests(client, gson).getRequest(GET_CATEGORIES)
}