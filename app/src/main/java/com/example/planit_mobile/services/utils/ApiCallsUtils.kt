package com.example.planit_mobile.services.utils

import android.util.Log
import com.example.planit_mobile.services.ErrorResponse
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

class ApiRequests(val client: OkHttpClient, val gson: Gson) {
    val setCookie = "Cookie"

    suspend inline fun <reified R> postRequest(
        url: String,
        data: String? = null,
        accessToken: String? = null,
        refreshToken: String? = null
    ): R {
        val req = Request.Builder().url(url)
            .addHeader("accept", "application/json")
            .addHeader(setCookie, "access_token=$accessToken")
            .addHeader(setCookie, "refresh_token=$refreshToken")
        if (data != null) {
            req.post(data.toRequestBody("application/json; charset=utf-8".toMediaType()))
        }
        return callApi(req.build())
    }

    suspend inline fun <reified R> putRequest(
        url: String,
        data: String,
        accessToken: String? = null,
        refreshToken: String? = null
    ): R {
        val req = Request.Builder().url(url)
            .addHeader("accept", "application/json")
            .addHeader(setCookie, "access_token=$accessToken")
            .addHeader(setCookie, "refresh_token=$refreshToken")
            .put(data.toRequestBody("application/json; charset=utf-8".toMediaType()))
        return callApi(req.build())
    }

    suspend inline fun <reified R> getRequest(
        url: String,
        accessToken: String? = null,
        refreshToken: String? = null,
    ): R {
        val req = Request.Builder().url(url).addHeader("accept", "application/json")
            .addHeader(setCookie, "access_token=$accessToken")
            .addHeader(setCookie, "refresh_token=$refreshToken")
            .build()
        return callApi(req)
    }

    suspend inline fun <reified R> deleteRequest(
        url: String,
        accessToken: String? = null,
        refreshToken: String? = null
    ): R {
        val req = Request.Builder().url(url).addHeader("accept", "application/json")
            .addHeader(setCookie, "access_token=$accessToken")
            .addHeader(setCookie, "refresh_token=$refreshToken")
            .delete()
            .build()
        return callApi(req)
    }


    suspend inline fun <reified R> callApi(
        request: Request
    ): R = suspendCancellableCoroutine {
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(Exception("Failure", e))
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyRes = response.body
                if (!response.isSuccessful || bodyRes == null) {
                    val errorResponse = gson.fromJson(bodyRes?.string(), ErrorResponse::class.java)
                    it.resumeWithException(Exception(" ${errorResponse.error}."))
                } else {
                    it.resumeWith(
                        Result.success(
                            gson.fromJson(
                                bodyRes.string(),
                                R::class.java,
                            ),
                        ),
                    )
                }
            }
        })
        it.invokeOnCancellation { call.cancel() }
    }
}