package com.shubhit.chatbotapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhit.chatbotapplication.model.NetworkError
import com.shubhit.chatbotapplication.model.NetworkResult
import com.shubhit.chatbotapplication.model.home
import com.shubhit.chatbotapplication.repository.HomePageRepository
import kotlinx.coroutines.launch

class HomePageViewModel:ViewModel() {
    val homePageResult=MutableLiveData<home>()
    val error=MutableLiveData<NetworkError>()

    fun getHomePageData(){
        viewModelScope.launch {
            when(val result=HomePageRepository.homePage()){
                is NetworkResult.Success->homePageResult.postValue(result.data)
                is NetworkResult.Error->error.postValue(result.networkError)
            }
        }
    }

}