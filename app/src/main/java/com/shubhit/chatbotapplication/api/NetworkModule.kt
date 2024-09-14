package com.shubhit.chatbotapplication.api

import com.shubhit.chatbotapplication.BuildConfig
import com.shubhit.chatbotapplication.utills.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {



    private val okHttpClient=OkHttpClient()
        .newBuilder()
        .connectTimeout(AppConstants.API_TIME_OUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(AppConstants.API_TIME_OUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(AppConstants.API_TIME_OUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(CustomInterceptor(BuildConfig.API_MASTER_KEY))
        .addInterceptor(run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        })
// .certificatePinner(certPinner)
        .build()


    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}