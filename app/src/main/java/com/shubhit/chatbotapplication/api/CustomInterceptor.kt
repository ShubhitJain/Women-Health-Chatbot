package com.shubhit.chatbotapplication.api

import com.shubhit.chatbotapplication.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


class CustomInterceptor(private val masterKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        newRequest.addHeader("content-type", "application/json")
        //  .addHeader("Authorization", "Bearer "+Prefrences.ReadToken())
            .addHeader("X-Master-Key", masterKey)
        return chain.proceed(newRequest.build())
    }
}