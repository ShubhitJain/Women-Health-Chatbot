package com.shubhit.chatbotapplication.api

import com.shubhit.chatbotapplication.BuildConfig
import com.shubhit.chatbotapplication.model.BotMessageModel
import com.shubhit.chatbotapplication.model.UserMessageModel
import com.shubhit.chatbotapplication.model.home
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers





interface ApiService {





    @GET("b/6666d1b9ad19ca34f876e822?meta=false")
   suspend fun homePage():Response<home>


    @GET("b/6667f621e41b4d34e401a1a5?meta=false")
    suspend fun chatMessages(
//        @Body userMessageModel: UserMessageModel
    ):Response<BotMessageModel>

}