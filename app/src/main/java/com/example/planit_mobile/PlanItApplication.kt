package com.example.planit_mobile

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import okhttp3.OkHttpClient
import androidx.datastore.preferences.core.Preferences
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.providers.UserProvider


/**
 * The contract to be supported by the application's class used to resolve dependencies.
 */
class PlanItApplication : Application(), PlanItDependencyProvider {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

    /**
     * The HTTP client used to perform HTTP requests
     */
    override val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .build()

    /**
     * The JSON serializer/deserializer used to convert JSON into DTOs
     */
    override val gson: Gson = Gson()

    /**
     * The service used to fetch user related info
     */
    override val userService: UserService = UserProvider(httpClient, gson)

    /**
     * The storage used to persist the user session
     */
    override val sessionStorage: SessionDataStore
        get() = SessionDataStore(dataStore)

}
