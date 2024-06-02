package com.example.planit_mobile.services.providers

import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.models.AuthOutputModel
import com.example.planit_mobile.services.models.EditUserInputModel
import com.example.planit_mobile.services.models.UserEventsResult
import com.example.planit_mobile.services.utils.ApiRequests
import com.example.planit_mobile.services.utils.PathTemplates.LOGIN_URL
import com.example.planit_mobile.services.utils.PathTemplates.LOGOUT_URL
import com.example.planit_mobile.services.utils.PathTemplates.REGISTER_URL
import com.example.planit_mobile.services.utils.PathTemplates.USER_EVENTS_URL
import com.example.planit_mobile.services.utils.PathTemplates.USER_URL
import com.google.gson.Gson
import okhttp3.OkHttpClient

/**
 * Provider that fetches user information from the PlanIt API.
 *
 * @param client The HTTP client used to perform the requests.
 * @param gson The JSON serializer/deserializer used to convert JSON into DTOs.
 */
class UserProvider(
    private val client: OkHttpClient,
    private val gson: Gson,
) : UserService {

    override suspend fun register(
        username: String,
        name: String,
        email: String,
        password: String
    ): AuthOutputModel =
        ApiRequests(client, gson).postRequest(
            REGISTER_URL,
            "{\"username\":\"$username\"," +
                    "\"name\":\"$name\"," +
                    "\"email\":\"$email\"," +
                    "\"password\":\"$password\"}"
        )

    override suspend fun login(emailOrName: String, password: String): AuthOutputModel =
        ApiRequests(client, gson).postRequest(
            LOGIN_URL,
            "{\"emailOrName\":\"$emailOrName\",\"password\":\"$password\"}"
        )

    override suspend fun logout(accessToken: String, refreshToken: String): Unit =
        ApiRequests(client, gson).postRequest(
            LOGOUT_URL,
            "{}",
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

    override suspend fun fetchUserInfo(
        uID: Int,
        userAccessToken: String,
        userRefreshToken: String
    ): User =
        ApiRequests(client, gson).getRequest(
            "$USER_URL/$uID",
            accessToken = userAccessToken,
            refreshToken = userRefreshToken
        )

    override suspend fun editUser(
        userAccessToken: String,
        userRefreshToken: String,
        name: String,
        interests: List<String>,
        description: String
    ) {
        val userData = EditUserInputModel(name, interests, description)
        return ApiRequests(client, gson).putRequest(
            USER_URL,
            gson.toJson(userData).toString(),
            accessToken = userAccessToken,
            refreshToken = userRefreshToken
        )
    }

    override suspend fun getUserEvents(
        userAccessToken: String,
        userRefreshToken: String
    ): UserEventsResult {
        return ApiRequests(client, gson).getRequest(
            USER_EVENTS_URL,
            accessToken = userAccessToken,
            refreshToken = userRefreshToken
        )
    }
}
