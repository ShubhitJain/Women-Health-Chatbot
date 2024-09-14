package com.shubhit.chatbotapplication.repository

import com.google.gson.GsonBuilder
import com.shubhit.chatbotapplication.api.ApiService
import com.shubhit.chatbotapplication.api.NetworkModule
import com.shubhit.chatbotapplication.model.BotMessageModel
import com.shubhit.chatbotapplication.model.ErrorBody
import com.shubhit.chatbotapplication.model.NetworkError
import com.shubhit.chatbotapplication.model.NetworkResult
import com.shubhit.chatbotapplication.model.UserMessageModel
import java.io.IOException

object ChatRepository {
    private val client: ApiService = NetworkModule.providesRetrofit().create(ApiService::class.java)
    suspend fun chatMessages(): NetworkResult<BotMessageModel> {
        return try {
            val response = ChatRepository.client.chatMessages()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body() as BotMessageModel)
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