package com.shubhit.chatbotapplication.repository

import com.google.gson.GsonBuilder
import com.shubhit.chatbotapplication.api.ApiService
import com.shubhit.chatbotapplication.api.NetworkModule
import com.shubhit.chatbotapplication.model.ErrorBody
import com.shubhit.chatbotapplication.model.NetworkError
import com.shubhit.chatbotapplication.model.NetworkResult
import com.shubhit.chatbotapplication.model.home
import java.io.IOException

object HomePageRepository {
    private val client: ApiService = NetworkModule.providesRetrofit().create(ApiService::class.java)

    suspend fun homePage(): NetworkResult<home> {
        return try {
            val response = client.homePage()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body() as home)
            } else {
                NetworkResult.Error(
                    NetworkError(
                        IOException("Error occurred"),
                        response.code(),
                        GsonBuilder().create().fromJson<ErrorBody>(
                            response.errorBody()!!.charStream(),
                            ErrorBody::class.java
                        ).message
                    )
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(
                NetworkError(
                    IOException(e.message),
                    0
                )
            )
        }


    }
}