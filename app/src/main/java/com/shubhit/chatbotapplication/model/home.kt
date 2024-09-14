package com.shubhit.chatbotapplication.model

data class home(
    val chat_request_icon: String,
    val chat_response_icon: String,
    val code: Int,
    val enable_avatar: Boolean,
    val favicon_icon: String,
    val is_mic_enable: String,
    val is_multilingual: String,
    val languages: List<Language>,
    val result: List<Result>,
    val site_logo: String,
    val site_title: String,
    val status: String
)