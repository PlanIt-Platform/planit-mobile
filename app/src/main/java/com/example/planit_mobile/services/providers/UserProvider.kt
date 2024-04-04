package com.example.planit_mobile.services.providers

import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.models.AuthOutputModel
import com.example.planit_mobile.services.models.EditUserInputModel
import com.example.planit_mobile.services.utils.ApiRequests
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resumeWithException

private const val PLANIT_API_URL = "http://10.0.2.2:1904/api-planit"
private const val REGISTER_URL = "$PLANIT_API_URL/register"
private const val LOGIN_URL = "$PLANIT_API_URL/login"
private const val LOGOUT_URL = "$PLANIT_API_URL/logout"
private const val USER_URL = "$PLANIT_API_URL/user"

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

    override suspend fun fetchUserInfo(uID: Int): User =
        ApiRequests(client, gson).getRequest(
            "$USER_URL/$uID"
        )

    override suspend fun editUser(uID: Int, name: String, interests: List<String>, description: String) {
        val userData = EditUserInputModel(name, interests, description)
        return ApiRequests(client, gson).putRequest(
            "$USER_URL/$uID/edit",
            gson.toJson(userData).toString()
        )
    }
}
