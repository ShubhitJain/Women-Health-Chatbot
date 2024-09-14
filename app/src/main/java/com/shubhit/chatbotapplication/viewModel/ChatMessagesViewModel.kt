package com.shubhit.chatbotapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhit.chatbotapplication.model.BotMessageModel
import com.shubhit.chatbotapplication.model.NetworkError
import com.shubhit.chatbotapplication.model.NetworkResult
import com.shubhit.chatbotapplication.model.UserMessageModel
import com.shubhit.chatbotapplication.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatMessagesViewModel:ViewModel() {
    val chatMessagesResult= MutableLiveData<BotMessageModel>()
    val error=MutableLiveData<NetworkError>()

    fun botMessage() {
        viewModelScope.launch {
            when (val result = ChatRepository.chatMessages()) {
                is NetworkResult.Success -> chatMessagesResult.postValue(result.data)
                is NetworkResult.Error-> error.postValue(result.networkError)

            }
        }
    }
}