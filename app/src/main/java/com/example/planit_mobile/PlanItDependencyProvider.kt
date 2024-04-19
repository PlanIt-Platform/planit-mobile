package com.example.planit_mobile

import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.UserService
import com.google.gson.Gson
import okhttp3.OkHttpClient


/**
 * The contract to be supported by the application's class used to resolve dependencies.
 */
interface PlanItDependencyProvider {
    /**
     * The HTTP client used to perform HTTP requests
     */
    val httpClient: OkHttpClient

    /**
     * The JSON serializer/deserializer used to convert JSON into DTOs
     */
    val gson: Gson

    /**
     * The service used to fetch user related info
     */
    val userService: UserService

    /**
     * The service used to fetch event related info
     */
    val eventService: EventService

    /**
     * The storage used to persist the user session
     */
    val sessionStorage: SessionDataStore
}
