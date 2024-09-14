package com.shubhit.chatbotapplication.model

data class ResultX(
    val follow_up_questions: List<String>,
    val query: String,
    val result: String,
    val source_documents: List<String>
)