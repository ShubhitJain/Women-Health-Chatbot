package com.shubhit.chatbotapplication.model


data class NetworkError(
    val exception: Exception,
    val statusCode: Int,
    val message: String = "Network Error Occurred, Please try again!"
)

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val networkError: NetworkError) : NetworkResult<Nothing>()
}

data class ErrorBody(
    val message: String
)



